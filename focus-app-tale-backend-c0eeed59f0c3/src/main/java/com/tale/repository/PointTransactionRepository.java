package com.tale.repository;

import com.tale.domain.PointTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PointTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {}
