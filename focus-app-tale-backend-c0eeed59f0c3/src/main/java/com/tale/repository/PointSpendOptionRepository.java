package com.tale.repository;

import com.tale.domain.PointSpendOption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PointSpendOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointSpendOptionRepository extends JpaRepository<PointSpendOption, Long> {}
