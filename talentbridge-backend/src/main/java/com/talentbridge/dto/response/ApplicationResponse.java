package com.talentbridge.dto.response;

import com.talentbridge.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponse {

    private Long id;

    private Long candidateId;

    private String candidateName;

    private Long vacancyId;

    private String vacancyTitle;

    private ApplicationStatus status;

    private LocalDateTime appliedDate;

}