package com.talentbridge.controller;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferRequest;
import com.talentbridge.dto.response.OfferResponse;
import com.talentbridge.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hr/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(
        OfferService offerService
    ) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferResponse> createDraft(
        @Valid
        @RequestBody
        CreateOfferRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                offerService.createDraft(request)
            );
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<OfferResponse> updateDraft(
        @PathVariable Long offerId,
        @Valid
        @RequestBody
        UpdateOfferRequest request
    ) {
        return ResponseEntity.ok(
            offerService.updateDraft(
                offerId,
                request
            )
        );
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOffer(
        @PathVariable Long offerId
    ) {
        return ResponseEntity.ok(
            offerService.getOfferById(offerId)
        );
    }

    @GetMapping(
        "/application/{applicationId}"
    )
    public ResponseEntity<OfferResponse>
        getOfferByApplication(
            @PathVariable Long applicationId
        ) {

        return ResponseEntity.ok(
            offerService.getOfferByApplicationId(
                applicationId
            )
        );
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>>
        getAllOffers() {

        return ResponseEntity.ok(
            offerService.getAllOffers()
        );
    }
}