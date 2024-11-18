package com.tale.repository;

import com.tale.domain.Analyticss;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Analyticss entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyticssRepository extends JpaRepository<Analyticss, Long> {}
