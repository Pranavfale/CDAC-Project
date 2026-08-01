package com.talentbridge.service.impl;

import org.slf4j.Logger;
import com.talentbridge.dto.response.ResumeDownload;
import com.talentbridge.exception.ResumeNotFoundException;
import com.talentbridge.storage.StoredResumeFile;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import com.talentbridge.dto.request.CandidateProfileRequest;
import com.talentbridge.dto.response.CandidateProfileResponse;
import com.talentbridge.entity.CandidateProfile;
import com.talentbridge.entity.User;
import com.talentbridge.enums.Role;
import com.talentbridge.exception.CandidateProfileNotFoundException;
import com.talentbridge.exception.DuplicateCandidateProfileException;
import com.talentbridge.mapper.CandidateProfileMapper;
import com.talentbridge.repository.CandidateProfileRepository;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.service.CandidateProfileService;
import com.talentbridge.storage.ResumeStorageService;
import com.talentbridge.storage.StoredResume;

/**
 * Implements business rules for authenticated Candidate Profile operations.
 *
 * Profile ownership is resolved from the authenticated user's email.
 * The frontend cannot choose the profile owner.
 */
@Service
@Transactional
public class CandidateProfileServiceImpl
        implements CandidateProfileService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CandidateProfileServiceImpl.class);

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final CandidateProfileMapper candidateProfileMapper;
    private final ResumeStorageService resumeStorageService;

    public CandidateProfileServiceImpl(
            CandidateProfileRepository candidateProfileRepository,
            UserRepository userRepository,
            CandidateProfileMapper candidateProfileMapper,
            ResumeStorageService resumeStorageService) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
        this.candidateProfileMapper = candidateProfileMapper;
        this.resumeStorageService = resumeStorageService;
    }

    /**
     * Returns the profile owned by the authenticated candidate.
     */
    @Override
    @Transactional(readOnly = true)
    public CandidateProfileResponse getProfile(
            String authenticatedEmail) {

        User candidate = getActiveCandidate(authenticatedEmail);

        CandidateProfile profile = getCandidateProfile(
                candidate.getId());

        return candidateProfileMapper.toResponse(profile);
    }

    /**
     * Creates one profile for the authenticated candidate.
     */
    @Override
    public CandidateProfileResponse createProfile(
            String authenticatedEmail,
            CandidateProfileRequest request) {

        User candidate = getActiveCandidate(authenticatedEmail);

        boolean profileExists = candidateProfileRepository
                .existsByUser_Id(candidate.getId());

        if (profileExists) {
            throw new DuplicateCandidateProfileException(
                    "Candidate profile already exists");
        }

        CandidateProfile profile =
                candidateProfileMapper.toEntity(
                        request,
                        candidate);

        profile.setProfileCompletion(
                calculateProfileCompletion(profile));

        CandidateProfile savedProfile =
                candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(savedProfile);
    }

    /**
     * Updates the editable fields of the authenticated candidate's profile.
     */
    @Override
    public CandidateProfileResponse updateProfile(
            String authenticatedEmail,
            CandidateProfileRequest request) {

        User candidate = getActiveCandidate(authenticatedEmail);

        CandidateProfile existingProfile =
                getCandidateProfile(candidate.getId());

        candidateProfileMapper.updateEntity(
                request,
                existingProfile);

        existingProfile.setProfileCompletion(
                calculateProfileCompletion(existingProfile));

        CandidateProfile savedProfile =
                candidateProfileRepository.save(existingProfile);

        return candidateProfileMapper.toResponse(savedProfile);
    }

    /**
     * Uploads or replaces the authenticated candidate's resume.
     *
     * Storage and database operations cannot be one physical transaction.
     * Therefore:
     *
     * - The new file is removed if the database transaction rolls back.
     * - The previous file is removed only after the transaction commits.
     */
    @Override
    public CandidateProfileResponse uploadResume(
            String authenticatedEmail,
            MultipartFile file) {

        User candidate = getActiveCandidate(authenticatedEmail);

        CandidateProfile profile =
                getCandidateProfile(candidate.getId());

        String previousStoredFileName =
                profile.getResumeFilePath();

        StoredResume storedResume =
                resumeStorageService.store(file);

        try {
            /*
             * resumeFileName contains the safe original display filename.
             *
             * resumeFilePath stores only the generated internal filename,
             * not the absolute filesystem path.
             */
            profile.setResumeFileName(
                    storedResume.originalFileName());

            profile.setResumeFilePath(
                    storedResume.storedFileName());

            profile.setProfileCompletion(
                    calculateProfileCompletion(profile));

            CandidateProfile savedProfile =
                    candidateProfileRepository.saveAndFlush(profile);

            registerResumeCleanup(
                    storedResume.storedFileName(),
                    previousStoredFileName);

            return candidateProfileMapper.toResponse(savedProfile);

        } catch (RuntimeException exception) {

            /*
             * saveAndFlush or transaction-registration failed before normal
             * completion. Remove the newly stored file to prevent an orphan.
             */
            deleteResumeQuietly(
                    storedResume.storedFileName(),
                    "new resume after database failure");

            throw exception;
        }
    }
    /**
     * Loads the authenticated candidate's resume for download.
     */
    @Override
    @Transactional(readOnly = true)
    public ResumeDownload downloadResume(
            String authenticatedEmail) {

        User candidate =
                getActiveCandidate(authenticatedEmail);

        CandidateProfile profile =
                getCandidateProfile(candidate.getId());

        if (!hasText(profile.getResumeFileName())
                || !hasText(profile.getResumeFilePath())) {

            throw new ResumeNotFoundException(
                    "Resume has not been uploaded");
        }

        StoredResumeFile storedResumeFile =
                resumeStorageService.load(
                        profile.getResumeFilePath());

        return new ResumeDownload(
                storedResumeFile.resource(),
                profile.getResumeFileName(),
                storedResumeFile.contentType(),
                storedResumeFile.contentLength());
    }
    /**
     * Loads a Candidate Profile using the authenticated user's ID.
     */
    private CandidateProfile getCandidateProfile(Long userId) {

        return candidateProfileRepository
                .findByUser_Id(userId)
                .orElseThrow(() ->
                        new CandidateProfileNotFoundException(
                                "Candidate profile not found"));
    }

    /**
     * Loads the authenticated user and verifies that the account belongs
     * to an active candidate.
     */
    private User getActiveCandidate(
            String authenticatedEmail) {

        String normalizedEmail =
                normalizeEmail(authenticatedEmail);

        User user = userRepository
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
                    "Only candidates can manage a candidate profile");
        }

        return user;
    }

    /**
     * Prevents a null or blank authentication identity from reaching
     * the database.
     */
    private String normalizeEmail(
            String authenticatedEmail) {

        if (authenticatedEmail == null
                || authenticatedEmail.isBlank()) {

            throw new AuthenticationCredentialsNotFoundException(
                    "Authenticated email is required");
        }

        return authenticatedEmail.trim();
    }

    /**
     * Registers filesystem cleanup that follows the database transaction.
     */
    private void registerResumeCleanup(
            String newStoredFileName,
            String previousStoredFileName) {

        boolean transactionSynchronizationAvailable =
                TransactionSynchronizationManager
                        .isSynchronizationActive()
                && TransactionSynchronizationManager
                        .isActualTransactionActive();

        if (!transactionSynchronizationAvailable) {

            /*
             * This method normally runs inside @Transactional.
             * This fallback supports direct invocation without a transaction.
             */
            deleteResumeQuietly(
                    previousStoredFileName,
                    "previous resume after replacement");

            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    /**
                     * The database now points to the new file, so the old
                     * stored file may be deleted safely.
                     */
                    @Override
                    public void afterCommit() {

                        deleteResumeQuietly(
                                previousStoredFileName,
                                "previous resume after successful replacement");
                    }

                    /**
                     * When the database rolls back, it still points to the
                     * previous file. The new file must therefore be removed.
                     */
                    @Override
                    public void afterCompletion(int status) {

                        if (status
                                == TransactionSynchronization
                                        .STATUS_ROLLED_BACK) {

                            deleteResumeQuietly(
                                    newStoredFileName,
                                    "new resume after transaction rollback");
                        }
                    }
                });
    }

    /**
     * Performs cleanup without replacing the original request result with
     * a secondary file-deletion error.
     */
    private void deleteResumeQuietly(
            String storedFileName,
            String cleanupContext) {

        try {
            resumeStorageService.deleteIfExists(storedFileName);

        } catch (RuntimeException exception) {

            LOGGER.warn(
                    "Could not delete {} with stored filename {}",
                    cleanupContext,
                    storedFileName,
                    exception);
        }
    }

    /**
     * Calculates the backend-controlled profile completion percentage.
     *
     * Eighteen core fields contribute five points each.
     * A stored resume contributes ten points.
     */
    private int calculateProfileCompletion(
            CandidateProfile profile) {

        int completion = 0;

        completion += hasText(profile.getHeadline()) ? 5 : 0;
        completion += profile.getDateOfBirth() != null ? 5 : 0;
        completion += hasText(profile.getGender()) ? 5 : 0;
        completion += hasText(profile.getAddress()) ? 5 : 0;
        completion += hasText(profile.getCity()) ? 5 : 0;
        completion += hasText(profile.getState()) ? 5 : 0;
        completion += hasText(profile.getPincode()) ? 5 : 0;

        completion += hasText(
                profile.getHighestQualification()) ? 5 : 0;

        completion += hasText(
                profile.getSpecialization()) ? 5 : 0;

        completion += hasText(profile.getCollege()) ? 5 : 0;
        completion += hasText(profile.getUniversity()) ? 5 : 0;
        completion += profile.getPassingYear() != null ? 5 : 0;

        completion += profile.getPercentageOrCgpa() != null
                ? 5
                : 0;

        completion += profile.getExperienceYears() != null
                ? 5
                : 0;

        completion += hasText(profile.getSkills()) ? 5 : 0;
        completion += hasText(profile.getLinkedinUrl()) ? 5 : 0;
        completion += hasText(profile.getGithubUrl()) ? 5 : 0;
        completion += hasText(profile.getProfileSummary()) ? 5 : 0;

        completion += hasText(profile.getResumeFileName())
                ? 10
                : 0;

        return Math.min(completion, 100);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}