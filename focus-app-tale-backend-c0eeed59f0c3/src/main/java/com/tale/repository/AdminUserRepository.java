package com.tale.repository;

import com.tale.domain.AdminUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdminUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {}
