package com.talentbridge.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.talentbridge.dto.response.PagedResponse;
import com.talentbridge.dto.response.VacancyResponse;
import com.talentbridge.enums.VacancyStatus;
import com.talentbridge.exception.GlobalExceptionHandler;
import com.talentbridge.exception.VacancyNotFoundException;
import com.talentbridge.service.VacancyService;

/**
 * MockMvc tests for anonymous public vacancy endpoints.
 *
 * These tests create only the controller layer. They do not start the
 * complete Spring Boot application or connect to MySQL.
 */
@ExtendWith(MockitoExtension.class)
class PublicVacancyControllerTest {

    private static final Long VACANCY_ID = 10L;

    private static final LocalDateTime OPENING_DATE =
            LocalDateTime.of(
                    2026,
                    8,
                    1,
                    10,
                    0);

    private static final LocalDateTime CLOSING_DATE =
            LocalDateTime.of(
                    2026,
                    8,
                    31,
                    23,
                    59);

    @Mock
    private VacancyService vacancyService;

    private MockMvc mockMvc;

    private VacancyResponse vacancyResponse;

    @BeforeEach
    void setUp() {

        PublicVacancyController controller =
                new PublicVacancyController(
                        vacancyService);

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(controller)
                        .setControllerAdvice(
                                new GlobalExceptionHandler())
                        .build();

        vacancyResponse =
                createVacancyResponse();
    }

    @Test
    @DisplayName(
        "GET public vacancies returns filtered paginated results"
    )
    void getPublicVacanciesReturnsFilteredPage()
            throws Exception {

        PagedResponse<VacancyResponse> pagedResponse =
                new PagedResponse<>(
                        List.of(vacancyResponse),
                        0,
                        5,
                        1L,
                        1,
                        true,
                        true);

        when(vacancyService.getPublicVacancies(
                "Java",
                "Pune",
                "FULL_TIME",
                0,
                5,
                "latest"))
                .thenReturn(pagedResponse);

        mockMvc.perform(
                get("/api/v1/public/vacancies")
                        .param(
                                "keyword",
                                "Java")
                        .param(
                                "location",
                                "Pune")
                        .param(
                                "employmentType",
                                "FULL_TIME")
                        .param(
                                "page",
                                "0")
                        .param(
                                "size",
                                "5")
                        .param(
                                "sort",
                                "latest")
                        .accept(
                                MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isOk())

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON))

                .andExpect(
                        jsonPath("$.content.length()")
                                .value(1))

                .andExpect(
                        jsonPath("$.content[0].id")
                                .value(VACANCY_ID))

                .andExpect(
                        jsonPath("$.content[0].title")
                                .value("Java Developer"))

                .andExpect(
                        jsonPath("$.content[0].description")
                                .value(
                                        "Spring Boot development role"))

                .andExpect(
                        jsonPath("$.content[0].location")
                                .value("Pune"))

                .andExpect(
                        jsonPath("$.content[0].employmentType")
                                .value("FULL_TIME"))

                .andExpect(
                        jsonPath("$.content[0].minExperience")
                                .value(1))

                .andExpect(
                        jsonPath("$.content[0].maxExperience")
                                .value(3))

                .andExpect(
                        jsonPath("$.content[0].minSalary")
                                .value(400000.0))

                .andExpect(
                        jsonPath("$.content[0].maxSalary")
                                .value(700000.0))

                .andExpect(
                        jsonPath("$.content[0].status")
                                .value("OPEN"))

                .andExpect(
                        jsonPath("$.page")
                                .value(0))

                .andExpect(
                        jsonPath("$.size")
                                .value(5))

                .andExpect(
                        jsonPath("$.totalElements")
                                .value(1))

                .andExpect(
                        jsonPath("$.totalPages")
                                .value(1))

                .andExpect(
                        jsonPath("$.first")
                                .value(true))

                .andExpect(
                        jsonPath("$.last")
                                .value(true));

        verify(vacancyService)
                .getPublicVacancies(
                        "Java",
                        "Pune",
                        "FULL_TIME",
                        0,
                        5,
                        "latest");
    }

    @Test
    @DisplayName(
        "GET public vacancies uses default page size and sorting"
    )
    void getPublicVacanciesUsesDefaultParameters()
            throws Exception {

        PagedResponse<VacancyResponse> emptyResponse =
                new PagedResponse<>(
                        List.of(),
                        0,
                        10,
                        0L,
                        0,
                        true,
                        true);

        when(vacancyService.getPublicVacancies(
                null,
                null,
                null,
                0,
                10,
                "latest"))
                .thenReturn(emptyResponse);

        mockMvc.perform(
                get("/api/v1/public/vacancies")
                        .accept(
                                MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isOk())

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON))

                .andExpect(
                        jsonPath("$.content")
                                .isArray())

                .andExpect(
                        jsonPath("$.content")
                                .isEmpty())

                .andExpect(
                        jsonPath("$.page")
                                .value(0))

                .andExpect(
                        jsonPath("$.size")
                                .value(10))

                .andExpect(
                        jsonPath("$.totalElements")
                                .value(0))

                .andExpect(
                        jsonPath("$.totalPages")
                                .value(0))

                .andExpect(
                        jsonPath("$.first")
                                .value(true))

                .andExpect(
                        jsonPath("$.last")
                                .value(true));

        verify(vacancyService)
                .getPublicVacancies(
                        null,
                        null,
                        null,
                        0,
                        10,
                        "latest");
    }

    @Test
    @DisplayName(
        "GET public vacancy by ID returns vacancy details"
    )
    void getPublicVacancyByIdReturnsDetails()
            throws Exception {

        when(vacancyService.getPublicVacancyById(
                VACANCY_ID))
                .thenReturn(vacancyResponse);

        mockMvc.perform(
                get(
                        "/api/v1/public/vacancies/{id}",
                        VACANCY_ID)
                        .accept(
                                MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isOk())

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON))

                .andExpect(
                        jsonPath("$.id")
                                .value(VACANCY_ID))

                .andExpect(
                        jsonPath("$.title")
                                .value("Java Developer"))

                .andExpect(
                        jsonPath("$.description")
                                .value(
                                        "Spring Boot development role"))

                .andExpect(
                        jsonPath("$.location")
                                .value("Pune"))

                .andExpect(
                        jsonPath("$.employmentType")
                                .value("FULL_TIME"))

                .andExpect(
                        jsonPath("$.minExperience")
                                .value(1))

                .andExpect(
                        jsonPath("$.maxExperience")
                                .value(3))

                .andExpect(
                        jsonPath("$.minSalary")
                                .value(400000.0))

                .andExpect(
                        jsonPath("$.maxSalary")
                                .value(700000.0))

                .andExpect(
                        jsonPath("$.status")
                                .value("OPEN"));

        verify(vacancyService)
                .getPublicVacancyById(
                        VACANCY_ID);
    }

    @Test
    @DisplayName(
        "GET unavailable public vacancy returns 404 error response"
    )
    void getPublicVacancyByIdReturnsNotFound()
            throws Exception {

        when(vacancyService.getPublicVacancyById(
                999L))
                .thenThrow(
                        new VacancyNotFoundException(
                                "Open vacancy not found"));

        mockMvc.perform(
                get(
                        "/api/v1/public/vacancies/{id}",
                        999L)
                        .accept(
                                MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isNotFound())

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON))

                .andExpect(
                        jsonPath("$.timestamp")
                                .exists())

                .andExpect(
                        jsonPath("$.status")
                                .value(404))

                .andExpect(
                        jsonPath("$.error")
                                .value("NOT_FOUND"))

                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Open vacancy not found"))

                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/public/vacancies/999"))

                .andExpect(
                        jsonPath("$.correlationId")
                                .doesNotExist());

        verify(vacancyService)
                .getPublicVacancyById(
                        999L);
    }

    @Test
    @DisplayName(
        "GET public vacancies returns 400 for invalid pagination"
    )
    void getPublicVacanciesReturnsBadRequestForInvalidPage()
            throws Exception {

        when(vacancyService.getPublicVacancies(
                null,
                null,
                null,
                -1,
                10,
                "latest"))
                .thenThrow(
                        new IllegalArgumentException(
                                "Page number cannot be negative"));

        mockMvc.perform(
                get("/api/v1/public/vacancies")
                        .param(
                                "page",
                                "-1")
                        .accept(
                                MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isBadRequest())

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON))

                .andExpect(
                        jsonPath("$.timestamp")
                                .exists())

                .andExpect(
                        jsonPath("$.status")
                                .value(400))

                .andExpect(
                        jsonPath("$.error")
                                .value("BAD_REQUEST"))

                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Page number cannot be negative"))

                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/public/vacancies"));

        verify(vacancyService)
                .getPublicVacancies(
                        null,
                        null,
                        null,
                        -1,
                        10,
                        "latest");
    }

    /**
     * Creates one response representing a currently available vacancy.
     */
    private VacancyResponse createVacancyResponse() {

        return VacancyResponse.builder()
                .id(VACANCY_ID)
                .title("Java Developer")
                .description(
                        "Spring Boot development role")
                .location("Pune")
                .employmentType("FULL_TIME")
                .minExperience(1)
                .maxExperience(3)
                .minSalary(400000.0)
                .maxSalary(700000.0)
                .openingDate(OPENING_DATE)
                .closingDate(CLOSING_DATE)
                .status(VacancyStatus.OPEN)
                .build();
    }
}