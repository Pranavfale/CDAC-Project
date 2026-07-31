package com.talentbridge.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores the extended profile information of one candidate user.
 *
 * Authentication fields such as email, password, role and account status remain
 * in the User entity. This entity stores only candidate-profile information.
 */
@Entity
@Table(
    name = "candidate_profiles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_candidate_profiles_user_id",
            columnNames = "user_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    /**
     * One candidate user can have at most one candidate profile.
     *
     * The unique user_id database constraint enforces this rule.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_candidate_profiles_user")
    )
    private User user;

    @Column(name = "headline", length = 255)
    private String headline;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 30)
    private String gender;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 20)
    private String pincode;

    @Column(name = "highest_qualification", length = 150)
    private String highestQualification;

    @Column(name = "specialization", length = 150)
    private String specialization;

    @Column(name = "college", length = 200)
    private String college;

    @Column(name = "university", length = 200)
    private String university;

    @Column(name = "passing_year")
    private Integer passingYear;

    /*
     * BigDecimal is used because both percentage and CGPA are decimal numeric
     * values. Detailed range validation will be added in the request DTO.
     */
    @PositiveOrZero(message = "Percentage or CGPA must not be negative")
    @Column(name = "percentage_or_cgpa", precision = 5, scale = 2)
    private BigDecimal percentageOrCgpa;

    @PositiveOrZero(message = "Experience years must not be negative")
    @Column(name = "experience_years", precision = 5, scale = 2)
    private BigDecimal experienceYears;

    @Column(name = "current_company", length = 200)
    private String currentCompany;

    @Column(name = "current_designation", length = 150)
    private String currentDesignation;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    /*
     * These values are internal storage metadata.
     * They must not be returned directly to React.
     */
    @Column(name = "resume_file_name", length = 255)
    private String resumeFileName;

    @Column(name = "resume_file_path", length = 1000)
    private String resumeFilePath;

    /*
     * Spring Boot will calculate this value.
     * React must never be treated as the authoritative source.
     */
    @Min(value = 0, message = "Profile completion cannot be less than 0")
    @Max(value = 100, message = "Profile completion cannot exceed 100")
    @Column(name = "profile_completion", nullable = false)
    private Integer profileCompletion = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Runs automatically before the profile is first inserted.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime currentTime = LocalDateTime.now();

        createdAt = currentTime;
        updatedAt = currentTime;

        if (profileCompletion == null) {
            profileCompletion = 0;
        }
    }

    /**
     * Runs automatically before an existing profile is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}