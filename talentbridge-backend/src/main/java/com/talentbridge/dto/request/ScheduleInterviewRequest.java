package com.talentbridge.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleInterviewRequest {

    private Long applicationId;

    private LocalDate interviewDate;

    private LocalTime interviewTime;

    private String mode;

    private String location;

}