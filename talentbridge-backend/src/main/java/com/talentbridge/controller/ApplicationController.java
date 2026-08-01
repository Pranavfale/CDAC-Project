package com.talentbridge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentbridge.dto.request.CreateApplicationRequest;
import com.talentbridge.dto.request.UpdateApplicationStatusRequest;
import com.talentbridge.dto.response.ApplicationResponse;
import com.talentbridge.service.ApplicationService;

import jakarta.validation.Valid;

/**
 * Provides secured Candidate and HR application APIs.
 */
@RestController
@RequestMapping("/api/v1")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(
            ApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    /**
     * POST /api/v1/candidate/applications
     *
     * Creates an application for the authenticated candidate.
     */
    @PostMapping("/candidate/applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> applyToVacancy(
            Authentication authentication,
            @Valid @RequestBody
            CreateApplicationRequest request) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        ApplicationResponse response =
                applicationService.applyToVacancy(
                        authenticatedEmail,
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * GET /api/v1/candidate/applications
     *
     * Returns applications belonging to the authenticated candidate.
     */
    @GetMapping("/candidate/applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponse>>
            getCandidateApplications(
                    Authentication authentication) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        return ResponseEntity.ok(
                applicationService
                        .getCandidateApplications(
                                authenticatedEmail));
    }

    /**
     * GET /api/v1/candidate/applications/{applicationId}
     *
     * Returns one application only when it belongs to the authenticated
     * candidate.
     */
    @GetMapping(
        "/candidate/applications/{applicationId}"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse>
            getCandidateApplication(
                    Authentication authentication,
                    @PathVariable Long applicationId) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        ApplicationResponse response =
                applicationService
                        .getCandidateApplication(
                                authenticatedEmail,
                                applicationId);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/candidate/applications/{applicationId}/withdraw
     *
     * Withdraws an application only when it belongs to the authenticated
     * candidate.
     */
    @PatchMapping(
        "/candidate/applications/{applicationId}/withdraw"
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse>
            withdrawApplication(
                    Authentication authentication,
                    @PathVariable Long applicationId) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        ApplicationResponse response =
                applicationService.withdrawApplication(
                        authenticatedEmail,
                        applicationId);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/hr/applications/vacancy/{vacancyId}
     *
     * Returns applications submitted for one vacancy.
     */
    @GetMapping(
        "/hr/applications/vacancy/{vacancyId}"
    )
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<ApplicationResponse>>
            getVacancyApplications(
                    @PathVariable Long vacancyId) {

        return ResponseEntity.ok(
                applicationService
                        .getVacancyApplications(
                                vacancyId));
    }

    /**
     * PUT /api/v1/hr/applications/{applicationId}/status
     *
     * Updates an application's recruitment status.
     */
    @PutMapping(
        "/hr/applications/{applicationId}/status"
    )
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody
            UpdateApplicationStatusRequest request) {

        applicationService.updateApplicationStatus(
                applicationId,
                request);

        return ResponseEntity.noContent().build();
    }

    /**
     * Returns the authenticated email stored by the JWT filter.
     */
    private String getAuthenticatedEmail(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {

            throw new AuthenticationCredentialsNotFoundException(
                    "Authentication is required");
        }

        return authentication.getName();
    }
}