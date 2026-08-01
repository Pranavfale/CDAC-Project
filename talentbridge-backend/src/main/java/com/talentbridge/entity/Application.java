package com.talentbridge.entity;

import com.talentbridge.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;


    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;


    @Column(nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();

}