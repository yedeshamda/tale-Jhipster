package com.tale.repository;

import com.tale.domain.UserCompanyRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserCompanyRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCompanyRoleRepository extends JpaRepository<UserCompanyRole, Long> {}
