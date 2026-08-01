package com.talentbridge.dto.request;

import com.talentbridge.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationStatusRequest {

    private ApplicationStatus status;

}