package com.talentbridge.service.impl;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferStatusRequest;
import com.talentbridge.dto.response.OfferResponse;
import com.talentbridge.service.OfferService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

	@Override
	public OfferResponse createOffer(CreateOfferRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfferResponse getOfferByApplicationId(Long applicationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OfferResponse updateOfferStatus(Long offerId, UpdateOfferStatusRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OfferResponse> getMyOffers() {
		// TODO Auto-generated method stub
		return null;
	}

}