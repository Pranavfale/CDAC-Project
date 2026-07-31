package com.talentbridge.service.impl;

import com.talentbridge.dto.request.CreateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyStatusRequest;
import com.talentbridge.dto.request.UpdateVacancyRequest;
import com.talentbridge.dto.response.VacancyResponse;
import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.VacancyStatus;
import com.talentbridge.repository.VacancyRepository;
import com.talentbridge.service.VacancyService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {


    private final VacancyRepository vacancyRepository;


    @Override
    public VacancyResponse createVacancy(CreateVacancyRequest request) {

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


        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        return mapToResponse(savedVacancy);
    }


    @Override
    public VacancyResponse getVacancyById(Long id) {

        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> 
                    new RuntimeException("Vacancy not found")
                );

        return mapToResponse(vacancy);
    }


    @Override
    public List<VacancyResponse> getAllVacancies() {

        return vacancyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public VacancyResponse updateVacancy(Long id,
                                         UpdateVacancyRequest request) {


        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Vacancy not found")
                );


        vacancy.setTitle(request.getTitle());
        vacancy.setDescription(request.getDescription());
        vacancy.setLocation(request.getLocation());
        vacancy.setEmploymentType(request.getEmploymentType());
        vacancy.setMinExperience(request.getMinExperience());
        vacancy.setMaxExperience(request.getMaxExperience());
        vacancy.setMinSalary(request.getMinSalary());
        vacancy.setMaxSalary(request.getMaxSalary());


        return mapToResponse(
                vacancyRepository.save(vacancy)
        );
    }


    @Override
    public void deleteVacancy(Long id) {

        vacancyRepository.deleteById(id);

    }


    private VacancyResponse mapToResponse(Vacancy vacancy) {

        return VacancyResponse.builder()
                .id(vacancy.getId())
                .title(vacancy.getTitle())
                .description(vacancy.getDescription())
                .location(vacancy.getLocation())
                .employmentType(vacancy.getEmploymentType())
                .minExperience(vacancy.getMinExperience())
                .maxExperience(vacancy.getMaxExperience())
                .minSalary(vacancy.getMinSalary())
                .maxSalary(vacancy.getMaxSalary())
                .openingDate(vacancy.getOpeningDate())
                .closingDate(vacancy.getClosingDate())
                .status(vacancy.getStatus())
                .build();
    }
    
    @Override
    public void updateVacancyStatus(Long id,
                                    UpdateVacancyStatusRequest request) {

        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vacancy not found")
                );


        vacancy.setStatus(request.getStatus());

        vacancyRepository.save(vacancy);
    }
    
    @Override
    public List<VacancyResponse> searchVacancies(String keyword) {

        return vacancyRepository.searchByKeyword(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<VacancyResponse> getOpenVacancies() {

        return vacancyRepository.findByStatus(VacancyStatus.OPEN)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}