package com.talentbridge.dto.response;

import com.talentbridge.enums.VacancyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacancyResponse {

    private Long id;

    private String title;

    private String description;

    private String location;

    private String employmentType;

    private Integer minExperience;

    private Integer maxExperience;

    private Double minSalary;

    private Double maxSalary;

    private LocalDateTime openingDate;

    private LocalDateTime closingDate;

    private VacancyStatus status;

}