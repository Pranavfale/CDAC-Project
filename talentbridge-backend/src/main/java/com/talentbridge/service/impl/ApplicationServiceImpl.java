package com.talentbridge.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentbridge.dto.request.CreateApplicationRequest;
import com.talentbridge.dto.request.UpdateApplicationStatusRequest;
import com.talentbridge.dto.response.ApplicationResponse;
import com.talentbridge.entity.Application;
import com.talentbridge.entity.CandidateProfile;
import com.talentbridge.entity.User;
import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.ApplicationStatus;
import com.talentbridge.enums.Role;
import com.talentbridge.enums.VacancyStatus;
import com.talentbridge.exception.ApplicationNotFoundException;
import com.talentbridge.exception.ApplicationWithdrawalNotAllowedException;
import com.talentbridge.exception.CandidateProfileNotFoundException;
import com.talentbridge.exception.DuplicateApplicationException;
import com.talentbridge.exception.ResumeNotFoundException;
import com.talentbridge.exception.VacancyNotAvailableException;
import com.talentbridge.exception.VacancyNotFoundException;
import com.talentbridge.repository.ApplicationRepository;
import com.talentbridge.repository.CandidateProfileRepository;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.repository.VacancyRepository;
import com.talentbridge.service.ApplicationService;

/**
 * Implements secure Candidate and HR application operations.
 */
@Service
@Transactional
public class ApplicationServiceImpl
        implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            VacancyRepository vacancyRepository,
            UserRepository userRepository,
            CandidateProfileRepository candidateProfileRepository) {

        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
        this.userRepository = userRepository;
        this.candidateProfileRepository =
                candidateProfileRepository;
    }

    /**
     * Creates an application using the authenticated candidate identity.
     */
    @Override
    public ApplicationResponse applyToVacancy(
            String authenticatedEmail,
            CreateApplicationRequest request) {

        if (request == null
                || request.getVacancyId() == null) {

            throw new IllegalArgumentException(
                    "Vacancy ID is required");
        }

        User candidate =
                getActiveCandidate(authenticatedEmail);

        Vacancy vacancy =
                getVacancy(request.getVacancyId());

        validateVacancyAvailability(vacancy);

        boolean alreadyApplied =
                applicationRepository
                        .existsByCandidate_IdAndVacancy_Id(
                                candidate.getId(),
                                vacancy.getId());

        if (alreadyApplied) {
            throw new DuplicateApplicationException(
                    "You have already applied to this vacancy");
        }

        CandidateProfile candidateProfile =
                candidateProfileRepository
                        .findByUser_Id(candidate.getId())
                        .orElseThrow(() ->
                                new CandidateProfileNotFoundException(
                                        "Create your candidate profile before applying"));

        if (!hasText(candidateProfile.getResumeFilePath())
                || !hasText(
                        candidateProfile.getResumeFileName())) {

            throw new ResumeNotFoundException(
                    "Upload a resume before applying to a vacancy");
        }

        Application application =
                Application.builder()
                        .candidate(candidate)
                        .vacancy(vacancy)
                        .resumeFilePath(
                                candidateProfile
                                        .getResumeFilePath())
                        .coverLetter(
                                normalizeCoverLetter(
                                        request.getCoverLetter()))
                        .status(ApplicationStatus.APPLIED)
                        .build();

        try {
            Application savedApplication =
                    applicationRepository
                            .saveAndFlush(application);

            return mapToResponse(savedApplication);

        } catch (DataIntegrityViolationException exception) {

            /*
             * The database unique constraint protects against two
             * simultaneous requests that pass the first duplicate check.
             */
            throw new DuplicateApplicationException(
                    "You have already applied to this vacancy");
        }
    }

    /**
     * Returns applications owned by the authenticated candidate.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getCandidateApplications(
            String authenticatedEmail) {

        User candidate =
                getActiveCandidate(authenticatedEmail);

        return applicationRepository
                .findByCandidate_IdOrderByAppliedDateDesc(
                        candidate.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Returns one application only when it belongs to the authenticated
     * candidate.
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse getCandidateApplication(
            String authenticatedEmail,
            Long applicationId) {

        User candidate =
                getActiveCandidate(authenticatedEmail);

        Application application =
                getCandidateOwnedApplication(
                        applicationId,
                        candidate.getId());

        return mapToResponse(application);
    }

    /**
     * Withdraws an application belonging to the authenticated candidate.
     */
    @Override
    public ApplicationResponse withdrawApplication(
            String authenticatedEmail,
            Long applicationId) {

        User candidate =
                getActiveCandidate(authenticatedEmail);

        Application application =
                getCandidateOwnedApplication(
                        applicationId,
                        candidate.getId());

        validateWithdrawalAllowed(
                application.getStatus());

        application.setStatus(
                ApplicationStatus.WITHDRAWN);

        /*
         * saveAndFlush ensures @PreUpdate updates updatedDate before the
         * response DTO is created.
         */
        Application savedApplication =
                applicationRepository
                        .saveAndFlush(application);

        return mapToResponse(savedApplication);
    }

    /**
     * Returns applications submitted for one vacancy.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getVacancyApplications(
            Long vacancyId) {

        getVacancy(vacancyId);

        return applicationRepository
                .findByVacancy_IdOrderByAppliedDateDesc(
                        vacancyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Updates an application's status.
     */
    @Override
    public void updateApplicationStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request) {

        validateApplicationId(applicationId);

        if (request == null
                || request.getStatus() == null) {

            throw new IllegalArgumentException(
                    "Application status is required");
        }

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Application not found"));

        application.setStatus(
                request.getStatus());

        applicationRepository.save(application);
    }

    /**
     * Loads an application only when it belongs to the supplied candidate.
     *
     * Returning the same not-found response for missing and unowned
     * applications prevents disclosure of another candidate's application.
     */
    private Application getCandidateOwnedApplication(
            Long applicationId,
            Long candidateId) {

        validateApplicationId(applicationId);

        return applicationRepository
                .findByIdAndCandidate_Id(
                        applicationId,
                        candidateId)
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found"));
    }

    /**
     * Validates whether the current application status may move to
     * WITHDRAWN.
     *
     * WITHDRAWN and REJECTED are terminal states.
     * HIRED applications cannot be withdrawn.
     */
    private void validateWithdrawalAllowed(
            ApplicationStatus currentStatus) {

        if (currentStatus == null) {
            throw new ApplicationWithdrawalNotAllowedException(
                    "Application status is unavailable");
        }

        switch (currentStatus) {

            case WITHDRAWN ->
                throw new ApplicationWithdrawalNotAllowedException(
                        "Application is already withdrawn");

            case HIRED ->
                throw new ApplicationWithdrawalNotAllowedException(
                        "A hired application cannot be withdrawn");

            case REJECTED ->
                throw new ApplicationWithdrawalNotAllowedException(
                        "A rejected application cannot be withdrawn");

            default -> {
                /*
                 * APPLIED, UNDER_REVIEW, SHORTLISTED,
                 * INTERVIEW_SCHEDULED, SELECTED and OFFERED may move
                 * to WITHDRAWN.
                 */
            }
        }
    }

    /**
     * Loads the authenticated user and verifies Candidate permissions.
     */
    private User getActiveCandidate(
            String authenticatedEmail) {

        String normalizedEmail =
                normalizeEmail(authenticatedEmail);

        User user =
                userRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Authenticated user not found"));

        if (!user.isActive()) {
            throw new AccessDeniedException(
                    "User account is inactive");
        }

        if (user.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException(
                    "Only candidates can manage candidate applications");
        }

        return user;
    }

    /**
     * Loads one vacancy.
     */
    private Vacancy getVacancy(
            Long vacancyId) {

        if (vacancyId == null || vacancyId <= 0) {
            throw new IllegalArgumentException(
                    "Valid vacancy ID is required");
        }

        return vacancyRepository
                .findById(vacancyId)
                .orElseThrow(() ->
                        new VacancyNotFoundException(
                                "Vacancy not found"));
    }

    /**
     * Checks that the vacancy is accepting applications.
     */
    private void validateVacancyAvailability(
            Vacancy vacancy) {

        if (vacancy.getStatus()
                != VacancyStatus.OPEN) {

            throw new VacancyNotAvailableException(
                    "Vacancy is not open for applications");
        }

        LocalDateTime currentTime =
                LocalDateTime.now();

        if (vacancy.getOpeningDate() != null
                && currentTime.isBefore(
                        vacancy.getOpeningDate())) {

            throw new VacancyNotAvailableException(
                    "Vacancy application period has not started");
        }

        if (vacancy.getClosingDate() != null
                && currentTime.isAfter(
                        vacancy.getClosingDate())) {

            throw new VacancyNotAvailableException(
                    "Vacancy application deadline has passed");
        }
    }

    /**
     * Validates an application path-variable value.
     */
    private void validateApplicationId(
            Long applicationId) {

        if (applicationId == null
                || applicationId <= 0) {

            throw new IllegalArgumentException(
                    "Valid application ID is required");
        }
    }

    /**
     * Validates the authentication identity.
     */
    private String normalizeEmail(
            String authenticatedEmail) {

        if (authenticatedEmail == null
                || authenticatedEmail.isBlank()) {

            throw new AuthenticationCredentialsNotFoundException(
                    "Authentication is required");
        }

        return authenticatedEmail.trim();
    }

    /**
     * Removes leading and trailing whitespace from an optional cover letter.
     */
    private String normalizeCoverLetter(
            String coverLetter) {

        if (coverLetter == null
                || coverLetter.isBlank()) {

            return null;
        }

        return coverLetter.trim();
    }

    /**
     * Creates a safe response without resume paths or HR notes.
     */
    private ApplicationResponse mapToResponse(
            Application application) {

        User candidate =
                application.getCandidate();

        Vacancy vacancy =
                application.getVacancy();

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateId(
                        candidate != null
                                ? candidate.getId()
                                : null)
                .candidateName(
                        candidate != null
                                ? candidate.getFullName()
                                : null)
                .vacancyId(
                        vacancy != null
                                ? vacancy.getId()
                                : null)
                .vacancyTitle(
                        vacancy != null
                                ? vacancy.getTitle()
                                : null)
                .vacancyLocation(
                        vacancy != null
                                ? vacancy.getLocation()
                                : null)
                .employmentType(
                        vacancy != null
                                ? vacancy.getEmploymentType()
                                : null)
                .coverLetter(
                        application.getCoverLetter())
                .status(
                        application.getStatus())
                .appliedDate(
                        application.getAppliedDate())
                .updatedDate(
                        application.getUpdatedDate())
                .build();
    }

    private boolean hasText(
            String value) {

        return value != null
                && !value.isBlank();
    }
}