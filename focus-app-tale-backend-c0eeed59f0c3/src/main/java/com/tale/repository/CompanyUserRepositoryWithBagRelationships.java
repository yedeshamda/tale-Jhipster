package com.tale.repository;

import com.tale.domain.CompanyUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CompanyUserRepositoryWithBagRelationships {
    Optional<CompanyUser> fetchBagRelationships(Optional<CompanyUser> companyUser);

    List<CompanyUser> fetchBagRelationships(List<CompanyUser> companyUsers);

    Page<CompanyUser> fetchBagRelationships(Page<CompanyUser> companyUsers);
}
