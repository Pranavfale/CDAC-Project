package com.talentbridge.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

/**
 * Implements the business rules for authenticated Candidate Profile
 * operations.
 *
 * Profile ownership is resolved from the authenticated user's email.
 * The frontend is not allowed to choose the owning user.
 */
@Service
@Transactional
public class CandidateProfileServiceImpl
        implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final CandidateProfileMapper candidateProfileMapper;

    public CandidateProfileServiceImpl(
            CandidateProfileRepository candidateProfileRepository,
            UserRepository userRepository,
            CandidateProfileMapper candidateProfileMapper) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
        this.candidateProfileMapper = candidateProfileMapper;
    }

    /**
     * Returns the profile owned by the authenticated candidate.
     */
    @Override
    @Transactional(readOnly = true)
    public CandidateProfileResponse getProfile(
            String authenticatedEmail) {

        User candidate = getActiveCandidate(authenticatedEmail);

        CandidateProfile profile = candidateProfileRepository
                .findByUser_Id(candidate.getId())
                .orElseThrow(() ->
                        new CandidateProfileNotFoundException(
                                "Candidate profile not found"));

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
                candidateProfileMapper.toEntity(request, candidate);

        profile.setProfileCompletion(
                calculateProfileCompletion(profile));

        CandidateProfile savedProfile =
                candidateProfileRepository.save(profile);

        return candidateProfileMapper.toResponse(savedProfile);
    }

    /**
     * Updates only the editable fields of the authenticated candidate's
     * existing profile.
     */
    @Override
    public CandidateProfileResponse updateProfile(
            String authenticatedEmail,
            CandidateProfileRequest request) {

        User candidate = getActiveCandidate(authenticatedEmail);

        CandidateProfile existingProfile =
                candidateProfileRepository
                        .findByUser_Id(candidate.getId())
                        .orElseThrow(() ->
                                new CandidateProfileNotFoundException(
                                        "Candidate profile not found"));

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
     * Calculates the backend-controlled profile completion percentage.
     *
     * Eighteen core profile fields contribute five points each.
     * A stored resume contributes ten points.
     *
     * Current company and designation are not counted because they may
     * legitimately be empty for fresher candidates.
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

    /**
     * Returns true when the supplied text contains a non-whitespace value.
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}