package com.talentbridge.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Safe response returned by Candidate Profile APIs.
 *
 * Passwords, JWT information and internal resume storage paths are
 * intentionally excluded.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileResponse {

    private Long profileId;

    private Long userId;

    private String fullName;

    private String email;

    private String headline;

    private LocalDate dateOfBirth;

    private String gender;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String highestQualification;

    private String specialization;

    private String college;

    private String university;

    private Integer passingYear;

    private BigDecimal percentageOrCgpa;

    private BigDecimal experienceYears;

    private String currentCompany;

    private String currentDesignation;

    private String skills;

    private String linkedinUrl;

    private String githubUrl;

    private String profileSummary;

    private boolean resumeUploaded;

    private String resumeFileName;

    private Integer profileCompletion;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}