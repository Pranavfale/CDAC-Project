package com.talentbridge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentbridge.dto.response.PagedResponse;
import com.talentbridge.dto.response.VacancyResponse;
import com.talentbridge.service.VacancyService;

/**
 * Provides vacancy browsing endpoints that do not require authentication.
 *
 * Only currently available OPEN vacancies are returned.
 */
@RestController
@RequestMapping("/api/v1/public/vacancies")
public class PublicVacancyController {

    private final VacancyService vacancyService;

    public PublicVacancyController(
            VacancyService vacancyService) {

        this.vacancyService = vacancyService;
    }

    /**
     * GET /api/v1/public/vacancies
     *
     * Supported query parameters:
     * - keyword
     * - location
     * - employmentType
     * - page
     * - size
     * - sort: latest or oldest
     */
    @GetMapping
    public ResponseEntity<PagedResponse<VacancyResponse>>
            getPublicVacancies(

                    @RequestParam(
                        required = false
                    )
                    String keyword,

                    @RequestParam(
                        required = false
                    )
                    String location,

                    @RequestParam(
                        required = false
                    )
                    String employmentType,

                    @RequestParam(
                        defaultValue = "0"
                    )
                    int page,

                    @RequestParam(
                        defaultValue = "10"
                    )
                    int size,

                    @RequestParam(
                        defaultValue = "latest"
                    )
                    String sort) {

        PagedResponse<VacancyResponse> response =
                vacancyService.getPublicVacancies(
                        keyword,
                        location,
                        employmentType,
                        page,
                        size,
                        sort);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/public/vacancies/{id}
     *
     * Returns one currently available OPEN vacancy.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponse>
            getPublicVacancyById(
                    @PathVariable Long id) {

        return ResponseEntity.ok(
                vacancyService
                        .getPublicVacancyById(id));
    }
}