package com.talentbridge.service;

import com.talentbridge.dto.request.ScheduleInterviewRequest;
import com.talentbridge.dto.request.UpdateInterviewStatusRequest;
import com.talentbridge.dto.response.InterviewResponse;

import java.util.List;

public interface InterviewService {


    InterviewResponse scheduleInterview(
            ScheduleInterviewRequest request
    );


    List<InterviewResponse> getApplicationInterview(
            Long applicationId
    );


    void updateInterviewStatus(
            Long interviewId,
            UpdateInterviewStatusRequest request
    );

}