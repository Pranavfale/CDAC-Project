package com.talentbridge.service;

import java.util.List;

import com.talentbridge.dto.request.CreateApplicationRequest;
import com.talentbridge.dto.request.UpdateApplicationStatusRequest;
import com.talentbridge.dto.response.ApplicationResponse;

/**
 * Defines Candidate and HR application operations.
 */
public interface ApplicationService {

    /**
     * Creates an application for the authenticated candidate.
     *
     * Candidate ownership is obtained from Spring Security and not from
     * frontend-supplied user IDs.
     *
     * @param authenticatedEmail authenticated candidate email
     * @param request vacancy and optional cover-letter information
     * @return newly created application
     */
    ApplicationResponse applyToVacancy(
            String authenticatedEmail,
            CreateApplicationRequest request);

    /**
     * Returns applications belonging to the authenticated candidate.
     *
     * @param authenticatedEmail authenticated candidate email
     * @return candidate applications ordered newest first
     */
    List<ApplicationResponse> getCandidateApplications(
            String authenticatedEmail);

    /**
     * Returns one application only when it belongs to the authenticated
     * candidate.
     *
     * @param authenticatedEmail authenticated candidate email
     * @param applicationId application database ID
     * @return owned application details
     */
    ApplicationResponse getCandidateApplication(
            String authenticatedEmail,
            Long applicationId);

    /**
     * Withdraws an application belonging to the authenticated candidate.
     *
     * @param authenticatedEmail authenticated candidate email
     * @param applicationId application database ID
     * @return application with WITHDRAWN status
     */
    ApplicationResponse withdrawApplication(
            String authenticatedEmail,
            Long applicationId);

    /**
     * Returns applications submitted for one vacancy.
     *
     * This operation is intended for HR and Admin users.
     *
     * @param vacancyId vacancy database ID
     * @return vacancy applications ordered newest first
     */
    List<ApplicationResponse> getVacancyApplications(
            Long vacancyId);

    /**
     * Updates an application's recruitment status.
     *
     * This operation is intended for HR and Admin users.
     *
     * @param applicationId application database ID
     * @param request new status
     */
    void updateApplicationStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request);
}