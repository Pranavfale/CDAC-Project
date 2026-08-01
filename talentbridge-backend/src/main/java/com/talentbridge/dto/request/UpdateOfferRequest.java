package com.talentbridge.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateOfferRequest {

    @NotNull(message = "Offered CTC is required")
    @DecimalMin(
        value = "0.01",
        message = "Offered CTC must be greater than zero"
    )
    private BigDecimal offeredCtc;

    @NotNull(message = "Joining date is required")
    @FutureOrPresent(
        message = "Joining date cannot be in the past"
    )
    private LocalDate joiningDate;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(
        message = "Expiry date cannot be in the past"
    )
    private LocalDate expiryDate;

    @NotBlank(message = "Department is required")
    @Size(max = 150)
    private String department;

    @NotBlank(message = "Employment type is required")
    @Pattern(
        regexp = "FULL_TIME|PART_TIME|CONTRACT|INTERNSHIP",
        message = "Employment type is invalid"
    )
    private String employmentType;

    @NotBlank(message = "Work location is required")
    @Size(max = 255)
    private String workLocation;

    @NotBlank(message = "Work mode is required")
    @Pattern(
        regexp = "ONSITE|HYBRID|REMOTE",
        message = "Work mode must be ONSITE, HYBRID, or REMOTE"
    )
    private String workMode;

    @Size(max = 50)
    private List<
        @NotBlank
        @Size(max = 500)
        String
    > benefits = new ArrayList<>();

    @Size(max = 50)
    private List<
        @NotBlank
        @Size(max = 2000)
        String
    > additionalTerms = new ArrayList<>();
}