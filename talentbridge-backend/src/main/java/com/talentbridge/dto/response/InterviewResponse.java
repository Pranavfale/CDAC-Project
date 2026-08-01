package com.talentbridge.dto.response;

import com.talentbridge.enums.InterviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class InterviewResponse {

    private Long id;

    private Long applicationId;

    private Long candidateId;

    private String candidateName;

    private String vacancyTitle;

    private LocalDate interviewDate;

    private LocalTime interviewTime;

    private String mode;

    private String location;

    private InterviewStatus status;

    private LocalDateTime createdAt;

}