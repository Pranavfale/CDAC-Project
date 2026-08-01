package com.talentbridge.mapper;

import org.springframework.stereotype.Component;

import com.talentbridge.dto.request.CandidateProfileRequest;
import com.talentbridge.dto.response.CandidateProfileResponse;
import com.talentbridge.entity.CandidateProfile;
import com.talentbridge.entity.User;

/**
 * Converts Candidate Profile request DTOs, entities and response DTOs.
 *
 * The mapper copies only candidate-editable request fields into the entity
 * and only safe fields into the response.
 */
@Component
public class CandidateProfileMapper {

    /**
     * Creates a new CandidateProfile entity using the authenticated user
     * and the candidate-editable request data.
     *
     * @param request candidate profile input
     * @param user authenticated candidate user
     * @return new CandidateProfile entity
     */
    public CandidateProfile toEntity(
            CandidateProfileRequest request,
            User user) {

        CandidateProfile profile = new CandidateProfile();
        profile.setUser(user);

        copyEditableFields(request, profile);

        return profile;
    }

    /**
     * Replaces the editable fields of an existing profile.
     *
     * The profile ID, owning user, resume path, profile completion and
     * timestamps are not accepted from the request.
     *
     * @param request candidate profile input
     * @param profile existing profile entity
     */
    public void updateEntity(
            CandidateProfileRequest request,
            CandidateProfile profile) {

        copyEditableFields(request, profile);
    }

    /**
     * Converts the entity into a safe response DTO.
     *
     * Internal information such as password and resumeFilePath is excluded.
     *
     * @param profile candidate profile entity
     * @return safe API response
     */
    public CandidateProfileResponse toResponse(
            CandidateProfile profile) {

        User user = profile.getUser();
        String resumeFileName = profile.getResumeFileName();

        return CandidateProfileResponse.builder()
                .profileId(profile.getProfileId())
                .userId(user != null ? user.getId() : null)
                .fullName(user != null ? user.getFullName() : null)
                .email(user != null ? user.getEmail() : null)
                .headline(profile.getHeadline())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .pincode(profile.getPincode())
                .highestQualification(profile.getHighestQualification())
                .specialization(profile.getSpecialization())
                .college(profile.getCollege())
                .university(profile.getUniversity())
                .passingYear(profile.getPassingYear())
                .percentageOrCgpa(profile.getPercentageOrCgpa())
                .experienceYears(profile.getExperienceYears())
                .currentCompany(profile.getCurrentCompany())
                .currentDesignation(profile.getCurrentDesignation())
                .skills(profile.getSkills())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .profileSummary(profile.getProfileSummary())
                .resumeUploaded(
                        resumeFileName != null
                                && !resumeFileName.isBlank())
                .resumeFileName(resumeFileName)
                .profileCompletion(profile.getProfileCompletion())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    /**
     * Copies only candidate-editable fields.
     */
    private void copyEditableFields(
            CandidateProfileRequest request,
            CandidateProfile profile) {

        profile.setHeadline(request.getHeadline());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPincode(request.getPincode());
        profile.setHighestQualification(
                request.getHighestQualification());
        profile.setSpecialization(request.getSpecialization());
        profile.setCollege(request.getCollege());
        profile.setUniversity(request.getUniversity());
        profile.setPassingYear(request.getPassingYear());
        profile.setPercentageOrCgpa(
                request.getPercentageOrCgpa());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setCurrentCompany(request.getCurrentCompany());
        profile.setCurrentDesignation(
                request.getCurrentDesignation());
        profile.setSkills(request.getSkills());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setProfileSummary(request.getProfileSummary());
    }
}