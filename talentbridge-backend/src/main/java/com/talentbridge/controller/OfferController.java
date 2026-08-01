package com.talentbridge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferStatusRequest;
import com.talentbridge.dto.response.OfferResponse;
import com.talentbridge.service.OfferService;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

	private final OfferService offerService;

	public OfferController(OfferService offerService) {
		this.offerService = offerService;
	}

	@PostMapping
	public ResponseEntity<OfferResponse> createOffer(@RequestBody CreateOfferRequest request) {

		return ResponseEntity.ok(offerService.createOffer(request));
	}

	@GetMapping("/application/{applicationId}")
	public ResponseEntity<OfferResponse> getOfferByApplication(@PathVariable Long applicationId) {

		return ResponseEntity.ok(offerService.getOfferByApplicationId(applicationId));
	}

	@PutMapping("/{offerId}/status")
	public ResponseEntity<OfferResponse> updateOfferStatus(@PathVariable Long offerId,
			@RequestBody UpdateOfferStatusRequest request) {

		return ResponseEntity.ok(offerService.updateOfferStatus(offerId, request));
	}

	@GetMapping("/my")
	public ResponseEntity<List<OfferResponse>> getMyOffers() {

		return ResponseEntity.ok(offerService.getMyOffers());
	}

}