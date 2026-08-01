package com.talentbridge.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateOfferRequest {

	private Long applicationId;

	private Double offeredSalary;

	private LocalDate joiningDate;

}