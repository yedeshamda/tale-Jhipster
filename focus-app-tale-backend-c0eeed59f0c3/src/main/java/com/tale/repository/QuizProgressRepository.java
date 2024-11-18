package com.tale.repository;

import com.tale.domain.QuizProgress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizProgress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuizProgressRepository extends JpaRepository<QuizProgress, Long> {}
