package com.tale.repository;

import com.tale.domain.Campaign;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Campaign entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {}
