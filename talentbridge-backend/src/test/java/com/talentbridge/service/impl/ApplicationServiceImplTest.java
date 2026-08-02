package com.talentbridge.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.talentbridge.dto.request.CreateApplicationRequest;
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
import com.talentbridge.repository.ApplicationRepository;
import com.talentbridge.repository.CandidateProfileRepository;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.repository.VacancyRepository;

/**
 * Unit tests for secure Candidate Application operations.
 *
 * These tests use Mockito and do not start Spring Boot, connect to MySQL,
 * or access the filesystem.
 */
@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    private static final String CANDIDATE_EMAIL =
            "candidate@example.com";

    private static final Long CANDIDATE_ID = 1L;

    private static final Long VACANCY_ID = 10L;

    private static final Long APPLICATION_ID = 100L;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidateProfileRepository candidateProfileRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private User candidate;

    private Vacancy openVacancy;

    private CandidateProfile candidateProfile;

    @BeforeEach
    void setUp() {

        candidate = createCandidate();

        openVacancy = createOpenVacancy();

        candidateProfile =
                createProfileWithResume();
    }

    @Test
    @DisplayName(
        "applyToVacancy creates an application for an eligible candidate"
    )
    void applyToVacancyCreatesApplicationSuccessfully() {

        CreateApplicationRequest request =
                createApplicationRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(vacancyRepository.findById(VACANCY_ID))
                .thenReturn(Optional.of(openVacancy));

        when(applicationRepository
                .existsByCandidate_IdAndVacancy_Id(
                        CANDIDATE_ID,
                        VACANCY_ID))
                .thenReturn(false);

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(candidateProfile));

        when(applicationRepository
                .saveAndFlush(any(Application.class)))
                .thenAnswer(invocation -> {

                    Application savedApplication =
                            invocation.getArgument(0);

                    savedApplication.setId(APPLICATION_ID);

                    return savedApplication;
                });

        ApplicationResponse response =
                applicationService.applyToVacancy(
                        CANDIDATE_EMAIL,
                        request);

        assertAll(
                () -> assertNotNull(response),

                () -> assertEquals(
                        APPLICATION_ID,
                        response.getId()),

                () -> assertEquals(
                        CANDIDATE_ID,
                        response.getCandidateId()),

                () -> assertEquals(
                        "Candidate User",
                        response.getCandidateName()),

                () -> assertEquals(
                        VACANCY_ID,
                        response.getVacancyId()),

                () -> assertEquals(
                        "Java Developer",
                        response.getVacancyTitle()),

                () -> assertEquals(
                        "Pune",
                        response.getVacancyLocation()),

                () -> assertEquals(
                        "FULL_TIME",
                        response.getEmploymentType()),

                () -> assertEquals(
                        "I am interested in this position.",
                        response.getCoverLetter()),

                () -> assertEquals(
                        ApplicationStatus.APPLIED,
                        response.getStatus())
        );

        ArgumentCaptor<Application> applicationCaptor =
                ArgumentCaptor.forClass(Application.class);

        verify(applicationRepository)
                .saveAndFlush(
                        applicationCaptor.capture());

        Application savedApplication =
                applicationCaptor.getValue();

        assertAll(
                () -> assertEquals(
                        candidate,
                        savedApplication.getCandidate()),

                () -> assertEquals(
                        openVacancy,
                        savedApplication.getVacancy()),

                () -> assertEquals(
                        "generated-resume.pdf",
                        savedApplication.getResumeFilePath()),

                () -> assertEquals(
                        "I am interested in this position.",
                        savedApplication.getCoverLetter()),

                () -> assertEquals(
                        ApplicationStatus.APPLIED,
                        savedApplication.getStatus())
        );
    }

    @Test
    @DisplayName(
        "applyToVacancy rejects duplicate candidate-vacancy applications"
    )
    void applyToVacancyRejectsDuplicateApplication() {

        CreateApplicationRequest request =
                createApplicationRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(vacancyRepository.findById(VACANCY_ID))
                .thenReturn(Optional.of(openVacancy));

        when(applicationRepository
                .existsByCandidate_IdAndVacancy_Id(
                        CANDIDATE_ID,
                        VACANCY_ID))
                .thenReturn(true);

        DuplicateApplicationException exception =
                assertThrows(
                        DuplicateApplicationException.class,
                        () ->
                                applicationService
                                        .applyToVacancy(
                                                CANDIDATE_EMAIL,
                                                request));

        assertEquals(
                "You have already applied to this vacancy",
                exception.getMessage());

        verify(candidateProfileRepository, never())
                .findByUser_Id(anyLong());

        verify(applicationRepository, never())
                .saveAndFlush(any(Application.class));
    }

    @Test
    @DisplayName(
        "applyToVacancy requires an existing Candidate Profile"
    )
    void applyToVacancyRejectsCandidateWithoutProfile() {

        CreateApplicationRequest request =
                createApplicationRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(vacancyRepository.findById(VACANCY_ID))
                .thenReturn(Optional.of(openVacancy));

        when(applicationRepository
                .existsByCandidate_IdAndVacancy_Id(
                        CANDIDATE_ID,
                        VACANCY_ID))
                .thenReturn(false);

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.empty());

        CandidateProfileNotFoundException exception =
                assertThrows(
                        CandidateProfileNotFoundException.class,
                        () ->
                                applicationService
                                        .applyToVacancy(
                                                CANDIDATE_EMAIL,
                                                request));

        assertEquals(
                "Create your candidate profile before applying",
                exception.getMessage());

        verify(applicationRepository, never())
                .saveAndFlush(any(Application.class));
    }

    @Test
    @DisplayName(
        "applyToVacancy requires an uploaded resume"
    )
    void applyToVacancyRejectsCandidateWithoutResume() {

        CreateApplicationRequest request =
                createApplicationRequest();

        candidateProfile.setResumeFileName(null);
        candidateProfile.setResumeFilePath(null);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(vacancyRepository.findById(VACANCY_ID))
                .thenReturn(Optional.of(openVacancy));

        when(applicationRepository
                .existsByCandidate_IdAndVacancy_Id(
                        CANDIDATE_ID,
                        VACANCY_ID))
                .thenReturn(false);

        when(candidateProfileRepository
                .findByUser_Id(CANDIDATE_ID))
                .thenReturn(Optional.of(candidateProfile));

        ResumeNotFoundException exception =
                assertThrows(
                        ResumeNotFoundException.class,
                        () ->
                                applicationService
                                        .applyToVacancy(
                                                CANDIDATE_EMAIL,
                                                request));

        assertEquals(
                "Upload a resume before applying to a vacancy",
                exception.getMessage());

        verify(applicationRepository, never())
                .saveAndFlush(any(Application.class));
    }

    @Test
    @DisplayName(
        "applyToVacancy rejects a closed vacancy"
    )
    void applyToVacancyRejectsClosedVacancy() {

        CreateApplicationRequest request =
                createApplicationRequest();

        openVacancy.setStatus(VacancyStatus.CLOSED);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(vacancyRepository.findById(VACANCY_ID))
                .thenReturn(Optional.of(openVacancy));

        VacancyNotAvailableException exception =
                assertThrows(
                        VacancyNotAvailableException.class,
                        () ->
                                applicationService
                                        .applyToVacancy(
                                                CANDIDATE_EMAIL,
                                                request));

        assertEquals(
                "Vacancy is not open for applications",
                exception.getMessage());

        verify(applicationRepository, never())
                .existsByCandidate_IdAndVacancy_Id(
                        anyLong(),
                        anyLong());

        verify(applicationRepository, never())
                .saveAndFlush(any(Application.class));
    }

    @Test
    @DisplayName(
        "getCandidateApplications returns only authenticated candidate applications"
    )
    void getCandidateApplicationsReturnsOwnedApplications() {

        Application application =
                createApplication(
                        ApplicationStatus.UNDER_REVIEW);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(applicationRepository
                .findByCandidate_IdOrderByAppliedDateDesc(
                        CANDIDATE_ID))
                .thenReturn(List.of(application));

        List<ApplicationResponse> responses =
                applicationService
                        .getCandidateApplications(
                                CANDIDATE_EMAIL);

        assertEquals(1, responses.size());

        ApplicationResponse response =
                responses.get(0);

        assertAll(
                () -> assertEquals(
                        APPLICATION_ID,
                        response.getId()),

                () -> assertEquals(
                        CANDIDATE_ID,
                        response.getCandidateId()),

                () -> assertEquals(
                        VACANCY_ID,
                        response.getVacancyId()),

                () -> assertEquals(
                        ApplicationStatus.UNDER_REVIEW,
                        response.getStatus())
        );

        verify(applicationRepository)
                .findByCandidate_IdOrderByAppliedDateDesc(
                        CANDIDATE_ID);
    }

    @Test
    @DisplayName(
        "getCandidateApplication hides missing or unowned applications"
    )
    void getCandidateApplicationRejectsUnownedApplication() {

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(applicationRepository
                .findByIdAndCandidate_Id(
                        APPLICATION_ID,
                        CANDIDATE_ID))
                .thenReturn(Optional.empty());

        ApplicationNotFoundException exception =
                assertThrows(
                        ApplicationNotFoundException.class,
                        () ->
                                applicationService
                                        .getCandidateApplication(
                                                CANDIDATE_EMAIL,
                                                APPLICATION_ID));

        assertEquals(
                "Application not found",
                exception.getMessage());
    }

    @Test
    @DisplayName(
        "withdrawApplication changes an owned application to WITHDRAWN"
    )
    void withdrawApplicationChangesStatusSuccessfully() {

        Application application =
                createApplication(
                        ApplicationStatus.SHORTLISTED);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(applicationRepository
                .findByIdAndCandidate_Id(
                        APPLICATION_ID,
                        CANDIDATE_ID))
                .thenReturn(Optional.of(application));

        when(applicationRepository
                .saveAndFlush(application))
                .thenReturn(application);

        ApplicationResponse response =
                applicationService.withdrawApplication(
                        CANDIDATE_EMAIL,
                        APPLICATION_ID);

        assertAll(
                () -> assertEquals(
                        ApplicationStatus.WITHDRAWN,
                        application.getStatus()),

                () -> assertEquals(
                        ApplicationStatus.WITHDRAWN,
                        response.getStatus())
        );

        verify(applicationRepository)
                .saveAndFlush(application);
    }

    @Test
    @DisplayName(
        "withdrawApplication rejects a HIRED application"
    )
    void withdrawApplicationRejectsHiredApplication() {

        Application application =
                createApplication(
                        ApplicationStatus.HIRED);

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        when(applicationRepository
                .findByIdAndCandidate_Id(
                        APPLICATION_ID,
                        CANDIDATE_ID))
                .thenReturn(Optional.of(application));

        ApplicationWithdrawalNotAllowedException exception =
                assertThrows(
                        ApplicationWithdrawalNotAllowedException.class,
                        () ->
                                applicationService
                                        .withdrawApplication(
                                                CANDIDATE_EMAIL,
                                                APPLICATION_ID));

        assertEquals(
                "A hired application cannot be withdrawn",
                exception.getMessage());

        assertEquals(
                ApplicationStatus.HIRED,
                application.getStatus());

        verify(applicationRepository, never())
                .saveAndFlush(any(Application.class));
    }

    @Test
    @DisplayName(
        "application operations reject an inactive candidate"
    )
    void applicationOperationsRejectInactiveCandidate() {

        candidate.setActive(false);

        CreateApplicationRequest request =
                createApplicationRequest();

        when(userRepository.findByEmail(CANDIDATE_EMAIL))
                .thenReturn(Optional.of(candidate));

        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () ->
                                applicationService
                                        .applyToVacancy(
                                                CANDIDATE_EMAIL,
                                                request));

        assertEquals(
                "User account is inactive",
                exception.getMessage());

        verifyNoInteractions(vacancyRepository);
        verifyNoInteractions(candidateProfileRepository);
        verifyNoInteractions(applicationRepository);
    }

    /**
     * Creates one active Candidate user.
     */
    private User createCandidate() {

        User user =
                new User();

        user.setId(CANDIDATE_ID);
        user.setFullName("Candidate User");
        user.setEmail(CANDIDATE_EMAIL);
        user.setPassword("encoded-password");
        user.setRole(Role.CANDIDATE);
        user.setActive(true);

        return user;
    }

    /**
     * Creates one currently available OPEN vacancy.
     */
    private Vacancy createOpenVacancy() {

        return Vacancy.builder()
                .id(VACANCY_ID)
                .title("Java Developer")
                .description(
                        "Spring Boot development position")
                .location("Pune")
                .employmentType("FULL_TIME")
                .minExperience(1)
                .maxExperience(3)
                .minSalary(400000.0)
                .maxSalary(700000.0)
                .openingDate(
                        LocalDateTime.now()
                                .minusDays(1))
                .closingDate(
                        LocalDateTime.now()
                                .plusDays(30))
                .status(VacancyStatus.OPEN)
                .build();
    }

    /**
     * Creates a Candidate Profile containing uploaded-resume metadata.
     */
    private CandidateProfile createProfileWithResume() {

        CandidateProfile profile =
                new CandidateProfile();

        profile.setProfileId(5L);
        profile.setUser(candidate);
        profile.setResumeFileName(
                "Candidate_Resume.pdf");
        profile.setResumeFilePath(
                "generated-resume.pdf");
        profile.setProfileCompletion(80);

        return profile;
    }

    /**
     * Creates a valid candidate application request.
     */
    private CreateApplicationRequest
            createApplicationRequest() {

        CreateApplicationRequest request =
                new CreateApplicationRequest();

        request.setVacancyId(VACANCY_ID);
        request.setCoverLetter(
                "  I am interested in this position.  ");

        return request;
    }

    /**
     * Creates one existing application with the requested status.
     */
    private Application createApplication(
            ApplicationStatus status) {

        return Application.builder()
                .id(APPLICATION_ID)
                .candidate(candidate)
                .vacancy(openVacancy)
                .resumeFilePath(
                        "generated-resume.pdf")
                .coverLetter(
                        "I am interested in this position.")
                .status(status)
                .appliedDate(
                        LocalDateTime.now()
                                .minusDays(2))
                .updatedDate(
                        LocalDateTime.now()
                                .minusDays(1))
                .build();
    }
}