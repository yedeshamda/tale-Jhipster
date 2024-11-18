package com.tale.repository;

import com.tale.domain.Responses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Responses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsesRepository extends JpaRepository<Responses, Long> {}
