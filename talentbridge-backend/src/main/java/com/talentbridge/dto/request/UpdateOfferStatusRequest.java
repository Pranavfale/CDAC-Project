package com.talentbridge.dto.request;

import com.talentbridge.enums.OfferStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOfferStatusRequest {

	private OfferStatus offerStatus;

}