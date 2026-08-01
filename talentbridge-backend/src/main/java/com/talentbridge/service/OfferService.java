package com.talentbridge.service;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferStatusRequest;
import com.talentbridge.dto.response.OfferResponse;

import java.util.List;

public interface OfferService {

	OfferResponse createOffer(CreateOfferRequest request);

	OfferResponse getOfferByApplicationId(Long applicationId);

	OfferResponse updateOfferStatus(Long offerId, UpdateOfferStatusRequest request);

	List<OfferResponse> getMyOffers();

}