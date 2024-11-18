package com.tale.repository;

import com.tale.domain.SurveyColl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Survey entity.
 *
 * When extending this class, extend SurveyRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */

@Repository
public interface SurveyCollRepository extends JpaRepository<SurveyColl, Long> {
    // Custom query methods if needed
}

