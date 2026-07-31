package com.talentbridge.service;

import com.talentbridge.dto.request.CreateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyRequest;
import com.talentbridge.dto.response.VacancyResponse;

import java.util.List;

public interface VacancyService {

    VacancyResponse createVacancy(CreateVacancyRequest request);

    VacancyResponse getVacancyById(Long id);

    List<VacancyResponse> getAllVacancies();

    VacancyResponse updateVacancy(Long id, UpdateVacancyRequest request);

    void deleteVacancy(Long id);

}