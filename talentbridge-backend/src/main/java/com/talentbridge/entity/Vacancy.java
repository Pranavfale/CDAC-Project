package com.talentbridge.entity;

import com.talentbridge.enums.VacancyStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(columnDefinition = "TEXT")
    private String description;


    private String location;


    private String employmentType;


    private Integer minExperience;


    private Integer maxExperience;


    private Double minSalary;


    private Double maxSalary;


    private LocalDateTime openingDate;


    private LocalDateTime closingDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacancyStatus status;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private User hr;
    
}