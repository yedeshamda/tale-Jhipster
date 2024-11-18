package com.tale.repository;

import com.tale.domain.OurDatabases;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OurDatabases entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OurDatabasesRepository extends JpaRepository<OurDatabases, Long> {}
