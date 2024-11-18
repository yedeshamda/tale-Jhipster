package com.tale.repository;

import com.tale.domain.CompanyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CompanyUserRepositoryWithBagRelationshipsImpl implements CompanyUserRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CompanyUser> fetchBagRelationships(Optional<CompanyUser> companyUser) {
        return companyUser.map(this::fetchUserRoles);
    }

    @Override
    public Page<CompanyUser> fetchBagRelationships(Page<CompanyUser> companyUsers) {
        return new PageImpl<>(
            fetchBagRelationships(companyUsers.getContent()),
            companyUsers.getPageable(),
            companyUsers.getTotalElements()
        );
    }

    @Override
    public List<CompanyUser> fetchBagRelationships(List<CompanyUser> companyUsers) {
        return Optional.of(companyUsers).map(this::fetchUserRoles).orElse(Collections.emptyList());
    }

    CompanyUser fetchUserRoles(CompanyUser result) {
        return entityManager
            .createQuery(
                "select companyUser from CompanyUser companyUser left join fetch companyUser.userRoles where companyUser.id = :id",
                CompanyUser.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<CompanyUser> fetchUserRoles(List<CompanyUser> companyUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, companyUsers.size()).forEach(index -> order.put(companyUsers.get(index).getId(), index));
        List<CompanyUser> result = entityManager
            .createQuery(
                "select companyUser from CompanyUser companyUser left join fetch companyUser.userRoles where companyUser in :companyUsers",
                CompanyUser.class
            )
            .setParameter("companyUsers", companyUsers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
