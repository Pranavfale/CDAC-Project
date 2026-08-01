package com.talentbridge.entity;

import com.talentbridge.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "offers",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_offers_application",
            columnNames = "application_id"
        )
    },
    indexes = {
        @Index(
            name = "idx_offers_candidate",
            columnList = "candidate_id"
        ),
        @Index(
            name = "idx_offers_vacancy",
            columnList = "vacancy_id"
        ),
        @Index(
            name = "idx_offers_status",
            columnList = "status"
        ),
        @Index(
            name = "idx_offers_created_by",
            columnList = "created_by_hr_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "application_id",
        nullable = false,
        unique = true
    )
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "candidate_id",
        nullable = false
    )
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "vacancy_id",
        nullable = false
    )
    private Vacancy vacancy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_hr_id")
    private User createdByHr;

    @Column(
        name = "position",
        nullable = false,
        length = 150
    )
    private String position;

    @Column(
        name = "department",
        length = 150
    )
    private String department;

    @Column(
        name = "offered_ctc",
        nullable = false,
        precision = 15,
        scale = 2
    )
    private BigDecimal offeredCtc;

    @Column(
        name = "joining_date",
        nullable = false
    )
    private LocalDate joiningDate;

    @Column(
        name = "expiry_date",
        nullable = false
    )
    private LocalDate expiryDate;

    @Column(
        name = "employment_type",
        nullable = false,
        length = 50
    )
    private String employmentType;

    @Column(
        name = "work_location",
        nullable = false,
        length = 255
    )
    private String workLocation;

    @Column(
        name = "work_mode",
        nullable = false,
        length = 50
    )
    private String workMode;

    @ElementCollection
    @CollectionTable(
        name = "offer_benefits",
        joinColumns = @JoinColumn(name = "offer_id")
    )
    @OrderColumn(name = "display_order")
    @Column(
        name = "benefit",
        nullable = false,
        length = 500
    )
    @Builder.Default
    private List<String> benefits = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "offer_additional_terms",
        joinColumns = @JoinColumn(name = "offer_id")
    )
    @OrderColumn(name = "display_order")
    @Column(
        name = "additional_term",
        nullable = false,
        columnDefinition = "TEXT"
    )
    @Builder.Default
    private List<String> additionalTerms =
        new ArrayList<>();

    @Column(name = "current_document_id")
    private Long currentDocumentId;

    @Column(
        name = "offer_letter_file_name",
        length = 255
    )
    private String offerLetterFileName;

    @Column(
        name = "offer_letter_path",
        length = 1000
    )
    private String offerLetterPath;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 30
    )
    @Builder.Default
    private OfferStatus offerStatus = OfferStatus.DRAFT;

    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    @Builder.Default
    private LocalDateTime createdAt =
        LocalDateTime.now();

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @PrePersist
    private void prepareForInsert() {
        if (application != null) {
            if (candidate == null) {
                candidate = application.getCandidate();
            }

            if (vacancy == null) {
                vacancy = application.getVacancy();
            }
        }

        if (vacancy != null) {
            if (position == null || position.isBlank()) {
                position = vacancy.getTitle();
            }

            if (
                employmentType == null
                    || employmentType.isBlank()
            ) {
                employmentType =
                    vacancy.getEmploymentType();
            }

            if (
                workLocation == null
                    || workLocation.isBlank()
            ) {
                workLocation = vacancy.getLocation();
            }

            if (
                createdByHr == null
                    && vacancy.getHr() != null
            ) {
                createdByHr = vacancy.getHr();
            }
        }

        offerStatus = OfferStatus.DRAFT;

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (benefits == null) {
            benefits = new ArrayList<>();
        }

        if (additionalTerms == null) {
            additionalTerms = new ArrayList<>();
        }
    }

    @Deprecated
    public Double getOfferedSalary() {
        return offeredCtc == null
            ? null
            : offeredCtc.doubleValue();
    }

    @Deprecated
    public void setOfferedSalary(
        Double offeredSalary
    ) {
        offeredCtc = offeredSalary == null
            ? null
            : BigDecimal.valueOf(offeredSalary);
    }
}