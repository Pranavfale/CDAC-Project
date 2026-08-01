package com.talentbridge.dto.response;

import java.time.LocalDateTime;

import com.talentbridge.enums.ApplicationStatus;

import lombok.Builder;
import lombok.Getter;

/**
 * Safe response returned for candidate application operations.
 *
 * Internal resume paths and HR notes are intentionally excluded.
 */
@Getter
@Builder
public class ApplicationResponse {

    private Long id;

    private Long candidateId;

    private String candidateName;

    private Long vacancyId;

    private String vacancyTitle;

    private String vacancyLocation;

    private String employmentType;

    private String coverLetter;

    private ApplicationStatus status;

    private LocalDateTime appliedDate;

    private LocalDateTime updatedDate;
}