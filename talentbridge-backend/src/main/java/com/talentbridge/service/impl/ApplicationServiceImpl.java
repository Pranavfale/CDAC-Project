package com.talentbridge.service.impl;

import com.talentbridge.dto.request.UpdateApplicationStatusRequest;
import com.talentbridge.dto.response.ApplicationResponse;
import com.talentbridge.entity.Application;
import com.talentbridge.entity.User;
import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.ApplicationStatus;
import com.talentbridge.repository.ApplicationRepository;
import com.talentbridge.repository.UserRepository;
import com.talentbridge.repository.VacancyRepository;
import com.talentbridge.service.ApplicationService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {


    private final ApplicationRepository applicationRepository;

    private final VacancyRepository vacancyRepository;

    private final UserRepository userRepository;


    @Override
    public ApplicationResponse applyToVacancy(
            Long vacancyId,
            Long candidateId) {


        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() ->
                        new RuntimeException("Vacancy not found")
                );


        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found")
                );


        Application application = Application.builder()
                .vacancy(vacancy)
                .candidate(candidate)
                .status(ApplicationStatus.APPLIED)
                .appliedDate(LocalDateTime.now())
                .build();


        return mapToResponse(
                applicationRepository.save(application)
        );
    }


    @Override
    public List<ApplicationResponse> getCandidateApplications(
            Long candidateId) {

        return applicationRepository.findByCandidateId(candidateId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<ApplicationResponse> getVacancyApplications(
            Long vacancyId) {

        return applicationRepository.findByVacancyId(vacancyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void updateApplicationStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request) {


        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new RuntimeException("Application not found")
                );


        application.setStatus(request.getStatus());

        applicationRepository.save(application);
    }


    private ApplicationResponse mapToResponse(Application application) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateId(application.getCandidate().getId())
                .candidateName(application.getCandidate().getFullName())
                .vacancyId(application.getVacancy().getId())
                .vacancyTitle(application.getVacancy().getTitle())
                .status(application.getStatus())
                .appliedDate(application.getAppliedDate())
                .build();
    }

}