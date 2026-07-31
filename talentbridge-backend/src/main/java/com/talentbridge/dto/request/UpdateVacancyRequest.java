package com.talentbridge.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVacancyRequest {


    @NotBlank(message = "Title is required")
    private String title;


    @NotBlank(message = "Description is required")
    private String description;


    private String location;


    private String employmentType;


    private Integer minExperience;


    private Integer maxExperience;


    private Double minSalary;


    private Double maxSalary;

}