package com.talentbridge.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentbridge.dto.request.CreateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyStatusRequest;
import com.talentbridge.dto.response.PagedResponse;
import com.talentbridge.dto.response.VacancyResponse;
import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.VacancyStatus;
import com.talentbridge.exception.VacancyNotFoundException;
import com.talentbridge.repository.VacancyRepository;
import com.talentbridge.service.VacancyService;

/**
 * Implements HR vacancy-management operations and public vacancy browsing.
 */
@Service
@Transactional
public class VacancyServiceImpl
        implements VacancyService {

    private static final int MAX_PUBLIC_PAGE_SIZE = 50;

    private final VacancyRepository vacancyRepository;

    public VacancyServiceImpl(
            VacancyRepository vacancyRepository) {

        this.vacancyRepository = vacancyRepository;
    }

    /**
     * Creates a draft vacancy.
     */
    @Override
    public VacancyResponse createVacancy(
            CreateVacancyRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Vacancy request is required");
        }

        Vacancy vacancy = Vacancy.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .minExperience(request.getMinExperience())
                .maxExperience(request.getMaxExperience())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .openingDate(LocalDateTime.now())
                .status(VacancyStatus.DRAFT)
                .build();

        Vacancy savedVacancy =
                vacancyRepository.save(vacancy);

        return mapToResponse(savedVacancy);
    }

    /**
     * Returns any vacancy by its ID.
     */
    @Override
    @Transactional(readOnly = true)
    public VacancyResponse getVacancyById(
            Long id) {

        return mapToResponse(
                getVacancy(id));
    }

    /**
     * Returns every vacancy.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VacancyResponse> getAllVacancies() {

        return vacancyRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Updates an existing vacancy.
     */
    @Override
    public VacancyResponse updateVacancy(
            Long id,
            UpdateVacancyRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Vacancy request is required");
        }

        Vacancy vacancy =
                getVacancy(id);

        vacancy.setTitle(
                request.getTitle());

        vacancy.setDescription(
                request.getDescription());

        vacancy.setLocation(
                request.getLocation());

        vacancy.setEmploymentType(
                request.getEmploymentType());

        vacancy.setMinExperience(
                request.getMinExperience());

        vacancy.setMaxExperience(
                request.getMaxExperience());

        vacancy.setMinSalary(
                request.getMinSalary());

        vacancy.setMaxSalary(
                request.getMaxSalary());

        Vacancy savedVacancy =
                vacancyRepository.save(vacancy);

        return mapToResponse(savedVacancy);
    }

    /**
     * Deletes an existing vacancy.
     */
    @Override
    public void deleteVacancy(
            Long id) {

        Vacancy vacancy =
                getVacancy(id);

        vacancyRepository.delete(vacancy);
    }

    /**
     * Updates a vacancy's status.
     */
    @Override
    public void updateVacancyStatus(
            Long id,
            UpdateVacancyStatusRequest request) {

        if (request == null
                || request.getStatus() == null) {

            throw new IllegalArgumentException(
                    "Vacancy status is required");
        }

        Vacancy vacancy =
                getVacancy(id);

        vacancy.setStatus(
                request.getStatus());

        vacancyRepository.save(vacancy);
    }

    /**
     * Performs the existing internal keyword search.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VacancyResponse> searchVacancies(
            String keyword) {

        String normalizedKeyword =
                normalizeOptionalFilter(keyword);

        if (normalizedKeyword == null) {
            throw new IllegalArgumentException(
                    "Search keyword is required");
        }

        return vacancyRepository
                .searchByKeyword(normalizedKeyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Returns vacancies having OPEN status.
     *
     * This existing internal operation does not apply public pagination.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VacancyResponse> getOpenVacancies() {

        return vacancyRepository
                .findByStatus(VacancyStatus.OPEN)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Returns publicly visible vacancies using safe filters and pagination.
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<VacancyResponse> getPublicVacancies(
            String keyword,
            String location,
            String employmentType,
            int page,
            int size,
            String sort) {

        validatePagination(page, size);

        String normalizedKeyword =
                normalizeOptionalFilter(keyword);

        String normalizedLocation =
                normalizeOptionalFilter(location);

        String normalizedEmploymentType =
                normalizeOptionalFilter(employmentType);

        PageRequest pageRequest =
                PageRequest.of(
                        page,
                        size,
                        buildPublicSort(sort));

        Page<VacancyResponse> responsePage =
                vacancyRepository
                        .findPublicVacancies(
                                VacancyStatus.OPEN,
                                LocalDateTime.now(),
                                normalizedKeyword,
                                normalizedLocation,
                                normalizedEmploymentType,
                                pageRequest)
                        .map(this::mapToResponse);

        return PagedResponse.from(responsePage);
    }

    /**
     * Returns one vacancy only when it is currently publicly visible.
     */
    @Override
    @Transactional(readOnly = true)
    public VacancyResponse getPublicVacancyById(
            Long id) {

        validateVacancyId(id);

        Vacancy vacancy =
                vacancyRepository
                        .findPublicVacancyById(
                                id,
                                VacancyStatus.OPEN,
                                LocalDateTime.now())
                        .orElseThrow(() ->
                                new VacancyNotFoundException(
                                        "Open vacancy not found"));

        return mapToResponse(vacancy);
    }

    /**
     * Loads an internal vacancy by ID.
     */
    private Vacancy getVacancy(
            Long id) {

        validateVacancyId(id);

        return vacancyRepository
                .findById(id)
                .orElseThrow(() ->
                        new VacancyNotFoundException(
                                "Vacancy not found"));
    }

    /**
     * Restricts user-controlled pagination values.
     */
    private void validatePagination(
            int page,
            int size) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1
                || size > MAX_PUBLIC_PAGE_SIZE) {

            throw new IllegalArgumentException(
                    "Page size must be between 1 and 50");
        }
    }

    /**
     * Builds sorting only from an approved whitelist.
     */
    private Sort buildPublicSort(
            String sort) {

        String normalizedSort =
                sort == null || sort.isBlank()
                        ? "latest"
                        : sort.trim()
                                .toLowerCase(Locale.ROOT);

        return switch (normalizedSort) {

            case "latest" ->
                Sort.by(
                        Sort.Order.desc("openingDate"),
                        Sort.Order.desc("id"));

            case "oldest" ->
                Sort.by(
                        Sort.Order.asc("openingDate"),
                        Sort.Order.asc("id"));

            default ->
                throw new IllegalArgumentException(
                        "Sort must be latest or oldest");
        };
    }

    /**
     * Converts blank optional filters into null values for the query.
     */
    private String normalizeOptionalFilter(
            String value) {

        if (value == null
                || value.isBlank()) {

            return null;
        }

        return value.trim();
    }

    /**
     * Validates a vacancy ID.
     */
    private void validateVacancyId(
            Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Valid vacancy ID is required");
        }
    }

    /**
     * Creates an external vacancy response.
     */
    private VacancyResponse mapToResponse(
            Vacancy vacancy) {

        return VacancyResponse.builder()
                .id(vacancy.getId())
                .title(vacancy.getTitle())
                .description(vacancy.getDescription())
                .location(vacancy.getLocation())
                .employmentType(
                        vacancy.getEmploymentType())
                .minExperience(
                        vacancy.getMinExperience())
                .maxExperience(
                        vacancy.getMaxExperience())
                .minSalary(
                        vacancy.getMinSalary())
                .maxSalary(
                        vacancy.getMaxSalary())
                .openingDate(
                        vacancy.getOpeningDate())
                .closingDate(
                        vacancy.getClosingDate())
                .status(
                        vacancy.getStatus())
                .build();
    }
}