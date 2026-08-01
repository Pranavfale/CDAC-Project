package com.talentbridge.controller;

import com.talentbridge.dto.request.UpdateApplicationStatusRequest;
import com.talentbridge.dto.response.ApplicationResponse;
import com.talentbridge.service.ApplicationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {


    private final ApplicationService applicationService;


    // Candidate applies for vacancy
    @PostMapping("/apply/{vacancyId}")
    public ResponseEntity<ApplicationResponse> applyToVacancy(
            @PathVariable Long vacancyId,
            @RequestParam Long candidateId) {


        return new ResponseEntity<>(
                applicationService.applyToVacancy(vacancyId, candidateId),
                HttpStatus.CREATED
        );
    }


    // Candidate views applications
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<ApplicationResponse>> getCandidateApplications(
            @PathVariable Long candidateId) {


        return ResponseEntity.ok(
                applicationService.getCandidateApplications(candidateId)
        );
    }


    // HR views applicants for vacancy
    @GetMapping("/vacancy/{vacancyId}")
    public ResponseEntity<List<ApplicationResponse>> getVacancyApplications(
            @PathVariable Long vacancyId) {


        return ResponseEntity.ok(
                applicationService.getVacancyApplications(vacancyId)
        );
    }


    // HR updates application status
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request) {


        applicationService.updateApplicationStatus(
                applicationId,
                request
        );

        return ResponseEntity.ok().build();
    }

}