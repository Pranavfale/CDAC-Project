package com.talentbridge.service;

import org.springframework.web.multipart.MultipartFile;
import com.talentbridge.dto.response.ResumeDownload;

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
     * @return authenticated candidate's profile
     */
    CandidateProfileResponse getProfile(String authenticatedEmail);

    /**
     * Creates one profile for the authenticated candidate.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @param request candidate-editable profile data
     * @return newly created profile
     */
    CandidateProfileResponse createProfile(
            String authenticatedEmail,
            CandidateProfileRequest request);

    /**
     * Updates the existing profile belonging to the authenticated candidate.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @param request candidate-editable profile data
     * @return updated profile
     */
    CandidateProfileResponse updateProfile(
            String authenticatedEmail,
            CandidateProfileRequest request);

    /**
     * Uploads or replaces the authenticated candidate's resume.
     *
     * The implementation validates and stores the file, updates the profile's
     * resume metadata, recalculates profile completion and removes the
     * previous stored file only after the database transaction succeeds.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @param file uploaded resume
     * @return profile containing updated resume information
     */
    CandidateProfileResponse uploadResume(
            String authenticatedEmail,
            MultipartFile file);
    /**
     * Downloads the resume belonging to the authenticated candidate.
     *
     * @param authenticatedEmail email obtained from Spring Security
     * @return secure resume download information
     */
    ResumeDownload downloadResume(
            String authenticatedEmail);
}