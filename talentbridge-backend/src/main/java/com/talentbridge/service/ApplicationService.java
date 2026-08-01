package com.talentbridge.service;

import com.talentbridge.dto.request.UpdateApplicationStatusRequest;
import com.talentbridge.dto.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {


    ApplicationResponse applyToVacancy(
            Long vacancyId,
            Long candidateId
    );


    List<ApplicationResponse> getCandidateApplications(
            Long candidateId
    );


    List<ApplicationResponse> getVacancyApplications(
            Long vacancyId
    );


    void updateApplicationStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request
    );

}