package com.talentbridge.dto.request;

import com.talentbridge.enums.VacancyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVacancyStatusRequest {

    @NotNull(message = "Status is required")
    private VacancyStatus status;

}