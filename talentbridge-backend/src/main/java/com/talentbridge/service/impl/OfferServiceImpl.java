package com.talentbridge.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferStatusRequest;
import com.talentbridge.dto.response.OfferResponse;
import com.talentbridge.entity.Application;
import com.talentbridge.entity.Offer;
import com.talentbridge.enums.OfferStatus;
import com.talentbridge.repository.ApplicationRepository;
import com.talentbridge.repository.OfferRepository;
import com.talentbridge.service.OfferService;

@Service
public class OfferServiceImpl implements OfferService {

	private final OfferRepository offerRepository;

	private final ApplicationRepository applicationRepository;

	public OfferServiceImpl(OfferRepository offerRepository, ApplicationRepository applicationRepository) {

		this.offerRepository = offerRepository;
		this.applicationRepository = applicationRepository;
	}

	@Override
	public OfferResponse createOffer(CreateOfferRequest request) {

		Application application = applicationRepository.findById(request.getApplicationId())
				.orElseThrow(() -> new RuntimeException("Application not found"));

		Offer offer = new Offer();

		offer.setApplication(application);
		offer.setOfferedSalary(request.getOfferedSalary());
		offer.setJoiningDate(request.getJoiningDate());
		offer.setOfferStatus(OfferStatus.SENT);

		Offer savedOffer = offerRepository.save(offer);

		return mapToResponse(savedOffer);
	}

	@Override
	public OfferResponse getOfferByApplicationId(Long applicationId) {

		Offer offer = offerRepository.findByApplicationId(applicationId)
				.orElseThrow(() -> new RuntimeException("Offer not found"));

		return mapToResponse(offer);
	}

	@Override
	public OfferResponse updateOfferStatus(Long offerId, UpdateOfferStatusRequest request) {

		Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new RuntimeException("Offer not found"));

		offer.setOfferStatus(request.getOfferStatus());

		Offer updatedOffer = offerRepository.save(offer);

		return mapToResponse(updatedOffer);
	}

	@Override
	public List<OfferResponse> getMyOffers() {

		return offerRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	private OfferResponse mapToResponse(Offer offer) {

		return new OfferResponse(offer.getId(), offer.getApplication().getId(), offer.getOfferedSalary(),
				offer.getJoiningDate(), offer.getOfferStatus());
	}

}