package com.talentbridge.entity;

import com.talentbridge.enums.InterviewStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false)
    private LocalDate interviewDate;


    @Column(nullable = false)
    private LocalTime interviewTime;


    private String mode;


    private String location;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;


    private LocalDateTime createdAt = LocalDateTime.now();

}