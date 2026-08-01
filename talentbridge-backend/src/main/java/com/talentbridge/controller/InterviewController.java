package com.talentbridge.controller;

import com.talentbridge.dto.request.ScheduleInterviewRequest;
import com.talentbridge.dto.request.UpdateInterviewStatusRequest;
import com.talentbridge.dto.response.InterviewResponse;
import com.talentbridge.service.InterviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {


    private final InterviewService interviewService;


    // HR schedules interview
    @PostMapping
    public ResponseEntity<InterviewResponse> scheduleInterview(
            @RequestBody ScheduleInterviewRequest request) {


        return new ResponseEntity<>(
                interviewService.scheduleInterview(request),
                HttpStatus.CREATED
        );
    }


    // View interview by application
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponse>> getApplicationInterview(
            @PathVariable Long applicationId) {


        return ResponseEntity.ok(
                interviewService.getApplicationInterview(applicationId)
        );
    }


    // Update interview status
    @PutMapping("/{interviewId}/status")
    public ResponseEntity<Void> updateInterviewStatus(
            @PathVariable Long interviewId,
            @RequestBody UpdateInterviewStatusRequest request) {


        interviewService.updateInterviewStatus(
                interviewId,
                request
        );

        return ResponseEntity.ok().build();
    }

}