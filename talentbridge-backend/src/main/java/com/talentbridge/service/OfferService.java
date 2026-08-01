package com.talentbridge.service;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferRequest;
import com.talentbridge.dto.response.OfferResponse;

import java.util.List;

public interface OfferService {

    OfferResponse createDraft(
        CreateOfferRequest request
    );

    OfferResponse updateDraft(
        Long offerId,
        UpdateOfferRequest request
    );

    OfferResponse getOfferById(
        Long offerId
    );

    OfferResponse getOfferByApplicationId(
        Long applicationId
    );

    List<OfferResponse> getAllOffers();
}