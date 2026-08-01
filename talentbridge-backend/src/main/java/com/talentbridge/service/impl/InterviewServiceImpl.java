package com.talentbridge.service.impl;

import com.talentbridge.dto.request.ScheduleInterviewRequest;
import com.talentbridge.dto.request.UpdateInterviewStatusRequest;
import com.talentbridge.dto.response.InterviewResponse;
import com.talentbridge.entity.Application;
import com.talentbridge.entity.Interview;
import com.talentbridge.enums.InterviewStatus;
import com.talentbridge.repository.ApplicationRepository;
import com.talentbridge.repository.InterviewRepository;
import com.talentbridge.service.InterviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {


    private final InterviewRepository interviewRepository;

    private final ApplicationRepository applicationRepository;


    @Override
    public InterviewResponse scheduleInterview(
            ScheduleInterviewRequest request) {


        Application application =
                applicationRepository.findById(request.getApplicationId())
                        .orElseThrow(() ->
                                new RuntimeException("Application not found")
                        );


        Interview interview = Interview.builder()
                .application(application)
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .mode(request.getMode())
                .location(request.getLocation())
                .status(InterviewStatus.SCHEDULED)
                .createdAt(LocalDateTime.now())
                .build();


        return mapToResponse(
                interviewRepository.save(interview)
        );
    }


    @Override
    public List<InterviewResponse> getApplicationInterview(
            Long applicationId) {

        return interviewRepository.findByApplicationId(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void updateInterviewStatus(
            Long interviewId,
            UpdateInterviewStatusRequest request) {


        Interview interview =
                interviewRepository.findById(interviewId)
                        .orElseThrow(() ->
                                new RuntimeException("Interview not found")
                        );


        interview.setStatus(request.getStatus());

        interviewRepository.save(interview);
    }


    private InterviewResponse mapToResponse(
            Interview interview) {


        return InterviewResponse.builder()
                .id(interview.getId())
                .applicationId(
                        interview.getApplication().getId()
                )
                .candidateId(
                        interview.getApplication()
                                .getCandidate()
                                .getId()
                )
                .candidateName(
                        interview.getApplication()
                                .getCandidate()
                                .getFullName()
                )
                .vacancyTitle(
                        interview.getApplication()
                                .getVacancy()
                                .getTitle()
                )
                .interviewDate(interview.getInterviewDate())
                .interviewTime(interview.getInterviewTime())
                .mode(interview.getMode())
                .location(interview.getLocation())
                .status(interview.getStatus())
                .createdAt(interview.getCreatedAt())
                .build();
    }

}