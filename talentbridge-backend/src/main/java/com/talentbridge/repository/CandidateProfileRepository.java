package com.talentbridge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentbridge.entity.CandidateProfile;

@Repository
public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    /**
     * Finds a candidate profile using the owning user's database ID.
     *
     * Spring Data interprets User_Id as:
     * CandidateProfile.user.id
     */
    Optional<CandidateProfile> findByUser_Id(Long userId);

    /**
     * Finds a candidate profile using the authenticated user's email.
     *
     * Spring Data interprets User_Email as:
     * CandidateProfile.user.email
     */
    Optional<CandidateProfile> findByUser_Email(String email);

    /**
     * Checks whether a profile already exists for the supplied user ID.
     * This prevents one candidate from creating multiple profiles.
     */
    boolean existsByUser_Id(Long userId);

    /**
     * Checks whether a profile already exists for the supplied email.
     */
    boolean existsByUser_Email(String email);
}