package com.talentbridge.dto.request;

import com.talentbridge.enums.InterviewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInterviewStatusRequest {

    private InterviewStatus status;

}