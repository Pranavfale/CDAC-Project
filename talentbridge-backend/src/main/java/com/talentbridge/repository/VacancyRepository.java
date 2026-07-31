package com.talentbridge.repository;

import com.talentbridge.entity.Vacancy;
import com.talentbridge.enums.VacancyStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
	
	List<Vacancy> findByStatus(VacancyStatus status);


	List<Vacancy> findByLocationContainingIgnoreCase(String location);


	@Query("""
	       SELECT v FROM Vacancy v
	       WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
	       OR LOWER(v.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
	       """)
	List<Vacancy> searchByKeyword(
	        @Param("keyword") String keyword
	);
	
}