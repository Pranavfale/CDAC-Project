package com.talentbridge.service;

import com.talentbridge.dto.request.CandidateProfileRequest;
import com.talentbridge.dto.response.CandidateProfileResponse;

/**
 * Defines the business operations available for an authenticated candidate's
 * extended recruitment profile.
 *
 * The authenticated user's email is supplied by the controller from the
 * Spring Security authentication object. A user ID must not be accepted from
 * React as the authoritative profile owner.
 */
public interface CandidateProfileService {

    /**
     * Returns the profile belonging to the authenticated candidate.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @return the authenticated candidate's safe profile response
     */
    CandidateProfileResponse getProfile(String authenticatedEmail);

    /**
     * Creates one profile for the authenticated candidate.
     *
     * The implementation must reject the request when a profile already
     * exists for the candidate.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @param request candidate-editable profile data
     * @return the newly created profile
     */
    CandidateProfileResponse createProfile(
            String authenticatedEmail,
            CandidateProfileRequest request);

    /**
     * Updates the existing profile belonging to the authenticated candidate.
     *
     * The implementation must not change the owning user, profile ID,
     * resume storage path, or timestamps based on frontend input.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @param request candidate-editable profile data
     * @return the updated profile
     */
    CandidateProfileResponse updateProfile(
            String authenticatedEmail,
            CandidateProfileRequest request);
}