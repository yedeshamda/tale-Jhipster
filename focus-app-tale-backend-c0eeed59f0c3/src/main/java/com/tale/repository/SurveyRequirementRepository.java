package com.tale.repository;

import com.tale.domain.SurveyRequirement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SurveyRequirement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyRequirementRepository extends JpaRepository<SurveyRequirement, Long> {}
