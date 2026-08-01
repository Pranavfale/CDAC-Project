package com.talentbridge.repository;

import com.talentbridge.entity.Offer;
import com.talentbridge.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfferRepository
    extends JpaRepository<Offer, Long> {

    Optional<Offer> findByApplicationId(
        Long applicationId
    );

    boolean existsByApplicationId(
        Long applicationId
    );

    Optional<Offer> findByIdAndCandidateId(
        Long offerId,
        Long candidateId
    );

    List<Offer> findAllByCandidateIdOrderByCreatedAtDesc(
        Long candidateId
    );

    List<Offer> findAllByCreatedByHrIdOrderByCreatedAtDesc(
        Long hrUserId
    );

    List<Offer> findAllByOfferStatusOrderByCreatedAtDesc(
        OfferStatus offerStatus
    );

    List<Offer> findAllByOfferStatusAndExpiryDateBefore(
        OfferStatus offerStatus,
        LocalDate expiryDate
    );
}