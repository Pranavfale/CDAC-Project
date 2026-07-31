package com.talentbridge.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVacancyRequest {

    @NotBlank(message = "Title is required")
    private String title;


    @NotBlank(message = "Description is required")
    private String description;


    @NotBlank(message = "Location is required")
    private String location;


    @NotBlank(message = "Employment type is required")
    private String employmentType;


    @Min(value = 0, message = "Experience cannot be negative")
    private Integer minExperience;


    @Min(value = 0, message = "Experience cannot be negative")
    private Integer maxExperience;


    private Double minSalary;


    private Double maxSalary;

}