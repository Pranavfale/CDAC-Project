package com.talentbridge.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.talentbridge.dto.response.PagedResponse;
import com.talentbridge.dto.response.VacancyResponse;
import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.VacancyStatus;
import com.talentbridge.exception.VacancyNotFoundException;
import com.talentbridge.repository.VacancyRepository;

/**
 * Unit tests for public vacancy browsing operations.
 *
 * These tests use Mockito and do not start Spring Boot or connect to MySQL.
 */
@ExtendWith(MockitoExtension.class)
class VacancyServiceImplTest {

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
    private VacancyRepository vacancyRepository;

    private VacancyServiceImpl vacancyService;

    private Vacancy openVacancy;

    @BeforeEach
    void setUp() {

        vacancyService =
                new VacancyServiceImpl(
                        vacancyRepository);

        openVacancy =
                createOpenVacancy();
    }

    @Test
    @DisplayName(
        "getPublicVacancies returns mapped paginated results with trimmed filters"
    )
    void getPublicVacanciesReturnsMappedPageWithTrimmedFilters() {

        PageRequest repositoryPageRequest =
                PageRequest.of(
                        0,
                        5);

        Page<Vacancy> vacancyPage =
                new PageImpl<>(
                        List.of(openVacancy),
                        repositoryPageRequest,
                        1);

        when(vacancyRepository.findPublicVacancies(
                eq(VacancyStatus.OPEN),
                any(LocalDateTime.class),
                eq("Java"),
                eq("Pune"),
                eq("FULL_TIME"),
                any(Pageable.class)))
                .thenReturn(vacancyPage);

        PagedResponse<VacancyResponse> response =
                vacancyService.getPublicVacancies(
                        "  Java  ",
                        "  Pune  ",
                        "  FULL_TIME  ",
                        0,
                        5,
                        "latest");

        assertNotNull(response);

        assertAll(
                () -> assertEquals(
                        1,
                        response.content().size()),

                () -> assertEquals(
                        0,
                        response.page()),

                () -> assertEquals(
                        5,
                        response.size()),

                () -> assertEquals(
                        1L,
                        response.totalElements()),

                () -> assertEquals(
                        1,
                        response.totalPages()),

                () -> assertTrue(
                        response.first()),

                () -> assertTrue(
                        response.last())
        );

        VacancyResponse vacancyResponse =
                response.content().get(0);

        assertAll(
                () -> assertEquals(
                        VACANCY_ID,
                        vacancyResponse.getId()),

                () -> assertEquals(
                        "Java Developer",
                        vacancyResponse.getTitle()),

                () -> assertEquals(
                        "Spring Boot development role",
                        vacancyResponse.getDescription()),

                () -> assertEquals(
                        "Pune",
                        vacancyResponse.getLocation()),

                () -> assertEquals(
                        "FULL_TIME",
                        vacancyResponse.getEmploymentType()),

                () -> assertEquals(
                        1,
                        vacancyResponse.getMinExperience()),

                () -> assertEquals(
                        3,
                        vacancyResponse.getMaxExperience()),

                () -> assertEquals(
                        400000.0,
                        vacancyResponse.getMinSalary()),

                () -> assertEquals(
                        700000.0,
                        vacancyResponse.getMaxSalary()),

                () -> assertEquals(
                        OPENING_DATE,
                        vacancyResponse.getOpeningDate()),

                () -> assertEquals(
                        CLOSING_DATE,
                        vacancyResponse.getClosingDate()),

                () -> assertEquals(
                        VacancyStatus.OPEN,
                        vacancyResponse.getStatus())
        );

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(
                        Pageable.class);

        verify(vacancyRepository)
                .findPublicVacancies(
                        eq(VacancyStatus.OPEN),
                        any(LocalDateTime.class),
                        eq("Java"),
                        eq("Pune"),
                        eq("FULL_TIME"),
                        pageableCaptor.capture());

        Pageable capturedPageable =
                pageableCaptor.getValue();

        assertAll(
                () -> assertEquals(
                        0,
                        capturedPageable.getPageNumber()),

                () -> assertEquals(
                        5,
                        capturedPageable.getPageSize())
        );

        Sort.Order openingDateOrder =
                capturedPageable
                        .getSort()
                        .getOrderFor("openingDate");

        Sort.Order idOrder =
                capturedPageable
                        .getSort()
                        .getOrderFor("id");

        assertAll(
                () -> assertNotNull(
                        openingDateOrder),

                () -> assertTrue(
                        openingDateOrder.isDescending()),

                () -> assertNotNull(
                        idOrder),

                () -> assertTrue(
                        idOrder.isDescending())
        );
    }

    @Test
    @DisplayName(
        "getPublicVacancies converts blank optional filters to null"
    )
    void getPublicVacanciesConvertsBlankFiltersToNull() {

        Page<Vacancy> emptyPage =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(
                                0,
                                10),
                        0);

        when(vacancyRepository.findPublicVacancies(
                eq(VacancyStatus.OPEN),
                any(LocalDateTime.class),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)))
                .thenReturn(emptyPage);

        PagedResponse<VacancyResponse> response =
                vacancyService.getPublicVacancies(
                        null,
                        "   ",
                        "",
                        0,
                        10,
                        null);

        assertNotNull(response);

        assertAll(
                () -> assertTrue(
                        response.content().isEmpty()),

                () -> assertEquals(
                        0L,
                        response.totalElements()),

                () -> assertEquals(
                        0,
                        response.totalPages())
        );

        verify(vacancyRepository)
                .findPublicVacancies(
                        eq(VacancyStatus.OPEN),
                        any(LocalDateTime.class),
                        isNull(),
                        isNull(),
                        isNull(),
                        any(Pageable.class));
    }

    @Test
    @DisplayName(
        "getPublicVacancies applies oldest-first sorting"
    )
    void getPublicVacanciesUsesOldestSorting() {

        Page<Vacancy> emptyPage =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(
                                1,
                                20),
                        0);

        when(vacancyRepository.findPublicVacancies(
                eq(VacancyStatus.OPEN),
                any(LocalDateTime.class),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)))
                .thenReturn(emptyPage);

        vacancyService.getPublicVacancies(
                null,
                null,
                null,
                1,
                20,
                "oldest");

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(
                        Pageable.class);

        verify(vacancyRepository)
                .findPublicVacancies(
                        eq(VacancyStatus.OPEN),
                        any(LocalDateTime.class),
                        isNull(),
                        isNull(),
                        isNull(),
                        pageableCaptor.capture());

        Pageable capturedPageable =
                pageableCaptor.getValue();

        Sort.Order openingDateOrder =
                capturedPageable
                        .getSort()
                        .getOrderFor("openingDate");

        Sort.Order idOrder =
                capturedPageable
                        .getSort()
                        .getOrderFor("id");

        assertAll(
                () -> assertEquals(
                        1,
                        capturedPageable.getPageNumber()),

                () -> assertEquals(
                        20,
                        capturedPageable.getPageSize()),

                () -> assertNotNull(
                        openingDateOrder),

                () -> assertTrue(
                        openingDateOrder.isAscending()),

                () -> assertNotNull(
                        idOrder),

                () -> assertTrue(
                        idOrder.isAscending())
        );
    }

    @Test
    @DisplayName(
        "getPublicVacancies rejects a negative page number"
    )
    void getPublicVacanciesRejectsNegativePageNumber() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                vacancyService
                                        .getPublicVacancies(
                                                null,
                                                null,
                                                null,
                                                -1,
                                                10,
                                                "latest"));

        assertEquals(
                "Page number cannot be negative",
                exception.getMessage());

        verifyNoInteractions(
                vacancyRepository);
    }

    @Test
    @DisplayName(
        "getPublicVacancies rejects page sizes outside 1 to 50"
    )
    void getPublicVacanciesRejectsInvalidPageSizes() {

        IllegalArgumentException zeroSizeException =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                vacancyService
                                        .getPublicVacancies(
                                                null,
                                                null,
                                                null,
                                                0,
                                                0,
                                                "latest"));

        IllegalArgumentException excessiveSizeException =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                vacancyService
                                        .getPublicVacancies(
                                                null,
                                                null,
                                                null,
                                                0,
                                                51,
                                                "latest"));

        assertAll(
                () -> assertEquals(
                        "Page size must be between 1 and 50",
                        zeroSizeException.getMessage()),

                () -> assertEquals(
                        "Page size must be between 1 and 50",
                        excessiveSizeException.getMessage())
        );

        verifyNoInteractions(
                vacancyRepository);
    }

    @Test
    @DisplayName(
        "getPublicVacancies rejects unsupported sorting"
    )
    void getPublicVacanciesRejectsUnsupportedSort() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                vacancyService
                                        .getPublicVacancies(
                                                null,
                                                null,
                                                null,
                                                0,
                                                10,
                                                "salary"));

        assertEquals(
                "Sort must be latest or oldest",
                exception.getMessage());

        verifyNoInteractions(
                vacancyRepository);
    }

    @Test
    @DisplayName(
        "getPublicVacancyById returns a currently visible open vacancy"
    )
    void getPublicVacancyByIdReturnsOpenVacancy() {

        when(vacancyRepository.findPublicVacancyById(
                eq(VACANCY_ID),
                eq(VacancyStatus.OPEN),
                any(LocalDateTime.class)))
                .thenReturn(
                        Optional.of(openVacancy));

        VacancyResponse response =
                vacancyService
                        .getPublicVacancyById(
                                VACANCY_ID);

        assertNotNull(response);

        assertAll(
                () -> assertEquals(
                        VACANCY_ID,
                        response.getId()),

                () -> assertEquals(
                        "Java Developer",
                        response.getTitle()),

                () -> assertEquals(
                        "Pune",
                        response.getLocation()),

                () -> assertEquals(
                        "FULL_TIME",
                        response.getEmploymentType()),

                () -> assertEquals(
                        VacancyStatus.OPEN,
                        response.getStatus())
        );

        verify(vacancyRepository)
                .findPublicVacancyById(
                        eq(VACANCY_ID),
                        eq(VacancyStatus.OPEN),
                        any(LocalDateTime.class));
    }

    @Test
    @DisplayName(
        "getPublicVacancyById hides missing or unavailable vacancies"
    )
    void getPublicVacancyByIdRejectsUnavailableVacancy() {

        when(vacancyRepository.findPublicVacancyById(
                eq(VACANCY_ID),
                eq(VacancyStatus.OPEN),
                any(LocalDateTime.class)))
                .thenReturn(
                        Optional.empty());

        VacancyNotFoundException exception =
                assertThrows(
                        VacancyNotFoundException.class,
                        () ->
                                vacancyService
                                        .getPublicVacancyById(
                                                VACANCY_ID));

        assertEquals(
                "Open vacancy not found",
                exception.getMessage());
    }

    @Test
    @DisplayName(
        "getPublicVacancyById rejects a non-positive vacancy ID"
    )
    void getPublicVacancyByIdRejectsInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                vacancyService
                                        .getPublicVacancyById(
                                                0L));

        assertEquals(
                "Valid vacancy ID is required",
                exception.getMessage());

        verifyNoInteractions(
                vacancyRepository);
    }

    /**
     * Creates one public OPEN vacancy.
     */
    private Vacancy createOpenVacancy() {

        return Vacancy.builder()
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