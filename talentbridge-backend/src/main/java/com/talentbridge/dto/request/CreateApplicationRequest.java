package com.talentbridge.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request submitted by an authenticated candidate when applying
 * for a vacancy.
 *
 * Candidate ID, resume path, status, HR notes, and timestamps are excluded.
 * The backend obtains the candidate from Spring Security and the resume from
 * the authenticated candidate's profile.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateApplicationRequest {

    @NotNull(message = "Vacancy ID is required")
    @Positive(message = "Vacancy ID must be positive")
    private Long vacancyId;

    @Size(
        max = 5000,
        message = "Cover letter cannot exceed 5000 characters"
    )
    private String coverLetter;
}