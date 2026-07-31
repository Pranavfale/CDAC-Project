package com.talentbridge.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentbridge.dto.request.CandidateProfileRequest;
import com.talentbridge.dto.response.CandidateProfileResponse;
import com.talentbridge.entity.CandidateProfile;
import com.talentbridge.entity.User;
import com.talentbridge.enums.Role;
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
                .orElseThrow(() -> new IllegalArgumentException(
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

        if (candidateProfileRepository
                .existsByUser_Id(candidate.getId())) {

            throw new IllegalStateException(
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
                        .orElseThrow(() -> new IllegalArgumentException(
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
     * Loads and validates the authenticated user.
     */
    private User getActiveCandidate(
            String authenticatedEmail) {

        String normalizedEmail =
                normalizeEmail(authenticatedEmail);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Authenticated user not found"));

        if (!user.isActive()) {
            throw new IllegalStateException(
                    "User account is inactive");
        }

        if (user.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException(
                    "Only candidates can manage a candidate profile");
        }

        return user;
    }

    /**
     * Prevents null or blank authentication identities from reaching the
     * repository.
     */
    private String normalizeEmail(
            String authenticatedEmail) {

        if (authenticatedEmail == null
                || authenticatedEmail.isBlank()) {

            throw new IllegalArgumentException(
                    "Authenticated email is required");
        }

        return authenticatedEmail.trim();
    }

    /**
     * Calculates a backend-controlled completion percentage.
     *
     * The project specification requires a completion percentage but does
     * not prescribe an exact weighting formula.
     *
     * This implementation assigns:
     *
     * - 5 points to each of 18 core profile fields = 90 points
     * - 10 points for a successfully stored resume
     *
     * Current company and current designation are not counted because they
     * may legitimately be empty for fresher candidates.
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