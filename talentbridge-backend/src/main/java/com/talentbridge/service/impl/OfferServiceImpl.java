package com.talentbridge.service.impl;

import com.talentbridge.dto.request.CreateOfferRequest;
import com.talentbridge.dto.request.UpdateOfferRequest;
import com.talentbridge.dto.response.OfferResponse;
import com.talentbridge.entity.Application;
import com.talentbridge.entity.Offer;
import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.ApplicationStatus;
import com.talentbridge.enums.OfferStatus;
import com.talentbridge.exception.ApplicationNotFoundException;
import com.talentbridge.exception.ApplicationNotSelectedException;
import com.talentbridge.exception.DuplicateOfferException;
import com.talentbridge.exception.InvalidOfferException;
import com.talentbridge.exception.OfferNotFoundException;
import com.talentbridge.repository.ApplicationRepository;
import com.talentbridge.repository.OfferRepository;
import com.talentbridge.service.OfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final ApplicationRepository applicationRepository;

    public OfferServiceImpl(
        OfferRepository offerRepository,
        ApplicationRepository applicationRepository
    ) {
        this.offerRepository = offerRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public OfferResponse createDraft(
        CreateOfferRequest request
    ) {
        Application application =
            applicationRepository
                .findById(request.getApplicationId())
                .orElseThrow(
                    () -> new ApplicationNotFoundException(
                        "Application not found with ID: "
                            + request.getApplicationId()
                    )
                );

        if (
            application.getStatus()
                != ApplicationStatus.SELECTED
        ) {
            throw new ApplicationNotSelectedException(
                "Only selected applications can receive an offer"
            );
        }

        if (
            offerRepository.existsByApplicationId(
                application.getId()
            )
        ) {
            throw new DuplicateOfferException(
                "An offer already exists for application ID: "
                    + application.getId()
            );
        }

        validateOffer(
            request.getOfferedCtc(),
            request.getJoiningDate(),
            request.getExpiryDate()
        );

        Vacancy vacancy = application.getVacancy();

        if (
            vacancy.getTitle() == null
                || vacancy.getTitle().isBlank()
        ) {
            throw new InvalidOfferException(
                "Vacancy position title is missing"
            );
        }

        Offer offer = Offer.builder()
            .application(application)
            .candidate(application.getCandidate())
            .vacancy(vacancy)
            .createdByHr(vacancy.getHr())
            .position(vacancy.getTitle().trim())
            .department(request.getDepartment().trim())
            .offeredCtc(request.getOfferedCtc())
            .joiningDate(request.getJoiningDate())
            .expiryDate(request.getExpiryDate())
            .employmentType(
                request.getEmploymentType().trim()
            )
            .workLocation(
                request.getWorkLocation().trim()
            )
            .workMode(request.getWorkMode().trim())
            .benefits(
                normalizeList(request.getBenefits())
            )
            .additionalTerms(
                normalizeList(
                    request.getAdditionalTerms()
                )
            )
            .offerStatus(OfferStatus.DRAFT)
            .build();

        return mapToResponse(
            offerRepository.save(offer)
        );
    }

    @Override
    public OfferResponse updateDraft(
        Long offerId,
        UpdateOfferRequest request
    ) {
        Offer offer = findOffer(offerId);

        if (offer.getOfferStatus() != OfferStatus.DRAFT) {
            throw new InvalidOfferException(
                "Only draft offers can be updated"
            );
        }

        validateOffer(
            request.getOfferedCtc(),
            request.getJoiningDate(),
            request.getExpiryDate()
        );

        offer.setDepartment(
            request.getDepartment().trim()
        );
        offer.setOfferedCtc(
            request.getOfferedCtc()
        );
        offer.setJoiningDate(
            request.getJoiningDate()
        );
        offer.setExpiryDate(
            request.getExpiryDate()
        );
        offer.setEmploymentType(
            request.getEmploymentType().trim()
        );
        offer.setWorkLocation(
            request.getWorkLocation().trim()
        );
        offer.setWorkMode(
            request.getWorkMode().trim()
        );
        offer.setBenefits(
            normalizeList(request.getBenefits())
        );
        offer.setAdditionalTerms(
            normalizeList(
                request.getAdditionalTerms()
            )
        );

        return mapToResponse(
            offerRepository.save(offer)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OfferResponse getOfferById(
        Long offerId
    ) {
        return mapToResponse(findOffer(offerId));
    }

    @Override
    @Transactional(readOnly = true)
    public OfferResponse getOfferByApplicationId(
        Long applicationId
    ) {
        Offer offer = offerRepository
            .findByApplicationId(applicationId)
            .orElseThrow(
                () -> new OfferNotFoundException(
                    "Offer not found for application ID: "
                        + applicationId
                )
            );

        return mapToResponse(offer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getAllOffers() {
        return offerRepository
            .findAllByOrderByCreatedAtDesc()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    private Offer findOffer(Long offerId) {
        return offerRepository
            .findById(offerId)
            .orElseThrow(
                () -> new OfferNotFoundException(
                    "Offer not found with ID: "
                        + offerId
                )
            );
    }

    private void validateOffer(
        BigDecimal offeredCtc,
        LocalDate joiningDate,
        LocalDate expiryDate
    ) {
        if (
            offeredCtc == null
                || offeredCtc.compareTo(
                    BigDecimal.ZERO
                ) <= 0
        ) {
            throw new InvalidOfferException(
                "Offered CTC must be greater than zero"
            );
        }

        if (
            joiningDate == null
                || expiryDate == null
        ) {
            throw new InvalidOfferException(
                "Joining date and expiry date are required"
            );
        }

        if (joiningDate.isBefore(LocalDate.now())) {
            throw new InvalidOfferException(
                "Joining date cannot be in the past"
            );
        }

        if (expiryDate.isBefore(LocalDate.now())) {
            throw new InvalidOfferException(
                "Expiry date cannot be in the past"
            );
        }

        if (!expiryDate.isBefore(joiningDate)) {
            throw new InvalidOfferException(
                "Expiry date must be before joining date"
            );
        }
    }

    private List<String> normalizeList(
        List<String> values
    ) {
        if (values == null) {
            return new ArrayList<>();
        }

        return values.stream()
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .distinct()
            .collect(
                Collectors.toCollection(
                    ArrayList::new
                )
            );
    }

    private OfferResponse mapToResponse(
        Offer offer
    ) {
        return OfferResponse.builder()
            .id(offer.getId())
            .applicationId(
                offer.getApplication().getId()
            )
            .candidateId(
                offer.getCandidate().getId()
            )
            .candidateName(
                offer.getCandidate().getFullName()
            )
            .vacancyId(
                offer.getVacancy().getId()
            )
            .position(offer.getPosition())
            .department(offer.getDepartment())
            .offeredCtc(offer.getOfferedCtc())
            .joiningDate(offer.getJoiningDate())
            .expiryDate(offer.getExpiryDate())
            .employmentType(
                offer.getEmploymentType()
            )
            .workLocation(
                offer.getWorkLocation()
            )
            .workMode(offer.getWorkMode())
            .benefits(
                new ArrayList<>(
                    offer.getBenefits()
                )
            )
            .additionalTerms(
                new ArrayList<>(
                    offer.getAdditionalTerms()
                )
            )
            .offerStatus(
                offer.getOfferStatus()
            )
            .createdAt(offer.getCreatedAt())
            .sentAt(offer.getSentAt())
            .respondedAt(
                offer.getRespondedAt()
            )
            .build();
    }
}