package com.tale.repository;

import com.tale.domain.SurveyInsight;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SurveyInsight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyInsightRepository extends JpaRepository<SurveyInsight, Long> {}
