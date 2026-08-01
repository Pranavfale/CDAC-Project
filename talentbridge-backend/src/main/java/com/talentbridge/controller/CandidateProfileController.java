package com.talentbridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talentbridge.dto.request.CandidateProfileRequest;
import com.talentbridge.dto.response.CandidateProfileResponse;
import com.talentbridge.service.CandidateProfileService;

import jakarta.validation.Valid;

/**
 * Provides Candidate Profile APIs for the authenticated candidate.
 *
 * Candidate ownership is determined using the authenticated email from
 * Spring Security. A user ID is never accepted from the frontend.
 */
@RestController
@RequestMapping("/api/v1/candidate/profile")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(
            CandidateProfileService candidateProfileService) {

        this.candidateProfileService = candidateProfileService;
    }

    /**
     * GET /api/v1/candidate/profile
     */
    @GetMapping
    public ResponseEntity<CandidateProfileResponse> getProfile(
            Authentication authentication) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        CandidateProfileResponse response =
                candidateProfileService.getProfile(
                        authenticatedEmail);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/candidate/profile
     */
    @PostMapping
    public ResponseEntity<CandidateProfileResponse> createProfile(
            Authentication authentication,
            @Valid @RequestBody CandidateProfileRequest request) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        CandidateProfileResponse response =
                candidateProfileService.createProfile(
                        authenticatedEmail,
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * PUT /api/v1/candidate/profile
     */
    @PutMapping
    public ResponseEntity<CandidateProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody CandidateProfileRequest request) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        CandidateProfileResponse response =
                candidateProfileService.updateProfile(
                        authenticatedEmail,
                        request);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/candidate/profile/resume
     *
     * The multipart request must contain a file part named "file".
     */
    @PostMapping(
        value = "/resume",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CandidateProfileResponse> uploadResume(
            Authentication authentication,
            @RequestPart("file") MultipartFile file) {

        String authenticatedEmail =
                getAuthenticatedEmail(authentication);

        CandidateProfileResponse response =
                candidateProfileService.uploadResume(
                        authenticatedEmail,
                        file);

        return ResponseEntity.ok(response);
    }

    /**
     * Safely extracts the authenticated user's email.
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