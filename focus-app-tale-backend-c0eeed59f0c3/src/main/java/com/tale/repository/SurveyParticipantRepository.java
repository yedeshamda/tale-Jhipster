package com.tale.repository;

import com.tale.domain.SurveyParticipant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SurveyParticipant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SurveyParticipantRepository extends JpaRepository<SurveyParticipant, Long> {}
