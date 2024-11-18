package com.tale.repository;

import com.tale.domain.FocusGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FocusGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FocusGroupRepository extends JpaRepository<FocusGroup, Long> {}
