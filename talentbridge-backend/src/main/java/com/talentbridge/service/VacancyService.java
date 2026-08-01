package com.talentbridge.service;

import java.util.List;

import com.talentbridge.dto.request.CreateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyStatusRequest;
import com.talentbridge.dto.response.PagedResponse;
import com.talentbridge.dto.response.VacancyResponse;

/**
 * Defines HR vacancy-management operations and public vacancy browsing.
 */
public interface VacancyService {

    /**
     * Creates a draft vacancy.
     */
    VacancyResponse createVacancy(
            CreateVacancyRequest request);

    /**
     * Returns any vacancy by ID for internal authenticated operations.
     */
    VacancyResponse getVacancyById(
            Long id);

    /**
     * Returns all vacancies for internal authenticated operations.
     */
    List<VacancyResponse> getAllVacancies();

    /**
     * Updates an existing vacancy.
     */
    VacancyResponse updateVacancy(
            Long id,
            UpdateVacancyRequest request);

    /**
     * Deletes a vacancy.
     */
    void deleteVacancy(
            Long id);

    /**
     * Updates a vacancy status.
     */
    void updateVacancyStatus(
            Long id,
            UpdateVacancyStatusRequest request);

    /**
     * Performs the existing internal keyword search.
     */
    List<VacancyResponse> searchVacancies(
            String keyword);

    /**
     * Returns all vacancies having OPEN status.
     */
    List<VacancyResponse> getOpenVacancies();

    /**
     * Returns currently visible public vacancies with optional filters.
     *
     * @param keyword optional title/description keyword
     * @param location optional location filter
     * @param employmentType optional employment-type filter
     * @param page zero-based page number
     * @param size page size between 1 and 50
     * @param sort latest or oldest
     */
    PagedResponse<VacancyResponse> getPublicVacancies(
            String keyword,
            String location,
            String employmentType,
            int page,
            int size,
            String sort);

    /**
     * Returns one currently visible public vacancy.
     */
    VacancyResponse getPublicVacancyById(
            Long id);
}