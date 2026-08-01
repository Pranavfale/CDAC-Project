package com.talentbridge.dto.response;

import com.talentbridge.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OfferResponse {

    private Long id;

    private Long applicationId;

    private Long candidateId;

    private String candidateName;

    private Long vacancyId;

    private String position;

    private String department;

    private BigDecimal offeredCtc;

    private LocalDate joiningDate;

    private LocalDate expiryDate;

    private String employmentType;

    private String workLocation;

    private String workMode;

    private List<String> benefits;

    private List<String> additionalTerms;

    private OfferStatus offerStatus;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private LocalDateTime respondedAt;
}