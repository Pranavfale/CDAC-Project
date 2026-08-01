package com.talentbridge.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.VacancyStatus;

/**
 * Provides database access for vacancies.
 */
@Repository
public interface VacancyRepository
        extends JpaRepository<Vacancy, Long> {

    /**
     * Returns vacancies having the supplied status.
     */
    List<Vacancy> findByStatus(
            VacancyStatus status);

    /**
     * Returns vacancies matching part of a location.
     */
    List<Vacancy> findByLocationContainingIgnoreCase(
            String location);

    /**
     * Existing internal keyword search.
     */
    @Query("""
            SELECT v
            FROM Vacancy v
            WHERE LOWER(v.title)
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(COALESCE(v.description, ''))
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Vacancy> searchByKeyword(
            @Param("keyword") String keyword);

    /**
     * Returns publicly visible vacancies.
     *
     * Public vacancies must:
     * - have OPEN status
     * - have started accepting applications
     * - not have passed their closing date
     * - satisfy all optional search filters
     */
    @Query(
        value = """
                SELECT v
                FROM Vacancy v
                WHERE v.status = :status
                  AND (
                        v.openingDate IS NULL
                        OR v.openingDate <= :currentTime
                  )
                  AND (
                        v.closingDate IS NULL
                        OR v.closingDate >= :currentTime
                  )
                  AND (
                        :keyword IS NULL
                        OR LOWER(v.title)
                            LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(v.description, ''))
                            LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                  AND (
                        :location IS NULL
                        OR LOWER(COALESCE(v.location, ''))
                            LIKE LOWER(CONCAT('%', :location, '%'))
                  )
                  AND (
                        :employmentType IS NULL
                        OR LOWER(COALESCE(v.employmentType, ''))
                            = LOWER(:employmentType)
                  )
                """,
        countQuery = """
                SELECT COUNT(v)
                FROM Vacancy v
                WHERE v.status = :status
                  AND (
                        v.openingDate IS NULL
                        OR v.openingDate <= :currentTime
                  )
                  AND (
                        v.closingDate IS NULL
                        OR v.closingDate >= :currentTime
                  )
                  AND (
                        :keyword IS NULL
                        OR LOWER(v.title)
                            LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(v.description, ''))
                            LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
                  AND (
                        :location IS NULL
                        OR LOWER(COALESCE(v.location, ''))
                            LIKE LOWER(CONCAT('%', :location, '%'))
                  )
                  AND (
                        :employmentType IS NULL
                        OR LOWER(COALESCE(v.employmentType, ''))
                            = LOWER(:employmentType)
                  )
                """
    )
    Page<Vacancy> findPublicVacancies(
            @Param("status")
            VacancyStatus status,

            @Param("currentTime")
            LocalDateTime currentTime,

            @Param("keyword")
            String keyword,

            @Param("location")
            String location,

            @Param("employmentType")
            String employmentType,

            Pageable pageable);

    /**
     * Returns one vacancy only when it is currently publicly visible.
     */
    @Query("""
            SELECT v
            FROM Vacancy v
            WHERE v.id = :vacancyId
              AND v.status = :status
              AND (
                    v.openingDate IS NULL
                    OR v.openingDate <= :currentTime
              )
              AND (
                    v.closingDate IS NULL
                    OR v.closingDate >= :currentTime
              )
            """)
    Optional<Vacancy> findPublicVacancyById(
            @Param("vacancyId")
            Long vacancyId,

            @Param("status")
            VacancyStatus status,

            @Param("currentTime")
            LocalDateTime currentTime);
}