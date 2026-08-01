package com.talentbridge.dto.response;

import com.talentbridge.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OfferResponse {

	private Long id;

	private Long applicationId;

	private Double offeredSalary;

	private LocalDate joiningDate;

	private OfferStatus offerStatus;

}