package com.talentbridge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentbridge.entity.Application;

/**
 * Provides database access for candidate applications.
 */
@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    /**
     * Retained temporarily for compatibility with the existing service.
     */
    List<Application> findByVacancyId(Long vacancyId);

    /**
     * Retained temporarily for compatibility with the existing service.
     */
    List<Application> findByCandidateId(Long candidateId);

    /**
     * Checks whether a candidate has already applied to a vacancy.
     */
    boolean existsByCandidate_IdAndVacancy_Id(
            Long candidateId,
            Long vacancyId);

    /**
     * Returns the authenticated candidate's applications,
     * newest application first.
     */
    List<Application> findByCandidate_IdOrderByAppliedDateDesc(
            Long candidateId);

    /**
     * Loads an application only when it belongs to the supplied candidate.
     *
     * This will be used for candidate application details and withdrawal.
     */
    Optional<Application> findByIdAndCandidate_Id(
            Long applicationId,
            Long candidateId);

    /**
     * Returns applications for one vacancy, newest first.
     */
    List<Application> findByVacancy_IdOrderByAppliedDateDesc(
            Long vacancyId);
}