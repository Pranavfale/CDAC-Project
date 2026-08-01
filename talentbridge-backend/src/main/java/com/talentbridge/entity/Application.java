package com.talentbridge.entity;

import java.time.LocalDateTime;

import com.talentbridge.enums.ApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents one candidate's application for one vacancy.
 *
 * Candidate ownership, vacancy ownership, status, resume metadata,
 * HR notes, and timestamps are controlled by the backend.
 */
@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_application_candidate_vacancy",
            columnNames = {
                "candidate_id",
                "vacancy_id"
            }
        )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    /**
     * Candidate who submitted the application.
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "candidate_id",
        nullable = false
    )
    private User candidate;

    /**
     * Vacancy for which the candidate applied.
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "vacancy_id",
        nullable = false
    )
    private Vacancy vacancy;

    /**
     * Generated internal resume filename captured when the candidate applies.
     *
     * This must contain only the generated stored filename, not an absolute
     * filesystem path.
     */
    @Column(
        name = "resume_path",
        length = 255
    )
    private String resumeFilePath;

    /**
     * Optional application-specific cover letter submitted by the candidate.
     */
    @Column(
        name = "cover_letter",
        columnDefinition = "TEXT"
    )
    private String coverLetter;

    /**
     * Internal notes recorded by authorized HR users.
     *
     * This field must not be exposed through candidate-facing responses.
     */
    @Column(
        name = "hr_notes",
        columnDefinition = "TEXT"
    )
    private String hrNotes;

    /**
     * Current backend-controlled application status.
     */
    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 40
    )
    @Builder.Default
    private ApplicationStatus status =
            ApplicationStatus.APPLIED;

    /**
     * Time when the candidate submitted the application.
     *
     * The Java name remains appliedDate for compatibility with the existing
     * ApplicationResponse and ApplicationServiceImpl.
     */
    @Column(
        name = "applied_at",
        nullable = false,
        updatable = false
    )
    @Builder.Default
    private LocalDateTime appliedDate =
            LocalDateTime.now();

    /**
     * Time when the application was last updated.
     */
    @Column(
        name = "updated_at",
        nullable = false
    )
    @Builder.Default
    private LocalDateTime updatedDate =
            LocalDateTime.now();

    /**
     * Initializes backend-controlled fields before insertion.
     */
    @PrePersist
    public void prePersist() {

        LocalDateTime now =
                LocalDateTime.now();

        if (status == null) {
            status = ApplicationStatus.APPLIED;
        }

        if (appliedDate == null) {
            appliedDate = now;
        }

        updatedDate = now;
    }

    /**
     * Updates the modification time before every database update.
     */
    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}