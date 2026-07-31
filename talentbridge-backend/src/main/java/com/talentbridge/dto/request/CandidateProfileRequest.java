package com.talentbridge.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains the editable candidate-profile fields received from React.
 *
 * Backend-controlled values such as user ID, profile ID, resume path,
 * profile completion and timestamps are intentionally excluded.
 */
@Getter
@Setter
@NoArgsConstructor
public class CandidateProfileRequest {

    @Size(
        max = 255,
        message = "Profile headline cannot exceed 255 characters"
    )
    private String headline;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(
        max = 30,
        message = "Gender cannot exceed 30 characters"
    )
    private String gender;

    @Size(
        max = 500,
        message = "Address cannot exceed 500 characters"
    )
    private String address;

    @Size(
        max = 100,
        message = "City cannot exceed 100 characters"
    )
    private String city;

    @Size(
        max = 100,
        message = "State cannot exceed 100 characters"
    )
    private String state;

    @Size(
        max = 20,
        message = "Pincode cannot exceed 20 characters"
    )
    private String pincode;

    @Size(
        max = 150,
        message = "Highest qualification cannot exceed 150 characters"
    )
    private String highestQualification;

    @Size(
        max = 150,
        message = "Specialization cannot exceed 150 characters"
    )
    private String specialization;

    @Size(
        max = 200,
        message = "College name cannot exceed 200 characters"
    )
    private String college;

    @Size(
        max = 200,
        message = "University name cannot exceed 200 characters"
    )
    private String university;

    @Min(
        value = 1900,
        message = "Passing year must be 1900 or later"
    )
    private Integer passingYear;

    /*
     * The entity column uses precision 5 and scale 2.
     * This allows up to three digits before the decimal point
     * and two digits after it.
     */
    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Percentage or CGPA must not be negative"
    )
    @Digits(
        integer = 3,
        fraction = 2,
        message = "Percentage or CGPA must contain at most 3 integer digits and 2 decimal digits"
    )
    private BigDecimal percentageOrCgpa;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Experience years must not be negative"
    )
    @Digits(
        integer = 3,
        fraction = 2,
        message = "Experience years must contain at most 3 integer digits and 2 decimal digits"
    )
    private BigDecimal experienceYears;

    @Size(
        max = 200,
        message = "Current company cannot exceed 200 characters"
    )
    private String currentCompany;

    @Size(
        max = 150,
        message = "Current designation cannot exceed 150 characters"
    )
    private String currentDesignation;

    @Size(
        max = 5000,
        message = "Skills cannot exceed 5000 characters"
    )
    private String skills;

    @Size(
        max = 500,
        message = "LinkedIn URL cannot exceed 500 characters"
    )
    @Pattern(
        regexp = "^$|^https?://.+$",
        message = "LinkedIn URL must begin with http:// or https://"
    )
    private String linkedinUrl;

    @Size(
        max = 500,
        message = "GitHub URL cannot exceed 500 characters"
    )
    @Pattern(
        regexp = "^$|^https?://.+$",
        message = "GitHub URL must begin with http:// or https://"
    )
    private String githubUrl;

    @Size(
        max = 5000,
        message = "Profile summary cannot exceed 5000 characters"
    )
    private String profileSummary;
}