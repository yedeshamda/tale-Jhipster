package com.tale.repository;

import com.tale.domain.NormalUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NormalUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NormalUserRepository extends JpaRepository<NormalUser, Long> {}
