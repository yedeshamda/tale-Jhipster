package com.tale.repository;

import com.tale.domain.NormalUser;
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
public class NormalUserRepositoryWithBagRelationshipsImpl implements NormalUserRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NormalUser> fetchBagRelationships(Optional<NormalUser> normalUser) {
        return normalUser.map(this::fetchParticipatedSurveys);
    }

    @Override
    public Page<NormalUser> fetchBagRelationships(Page<NormalUser> normalUsers) {
        return new PageImpl<>(fetchBagRelationships(normalUsers.getContent()), normalUsers.getPageable(), normalUsers.getTotalElements());
    }

    @Override
    public List<NormalUser> fetchBagRelationships(List<NormalUser> normalUsers) {
        return Optional.of(normalUsers).map(this::fetchParticipatedSurveys).orElse(Collections.emptyList());
    }

    NormalUser fetchParticipatedSurveys(NormalUser result) {
        return entityManager
            .createQuery(
                "select normalUser from NormalUser normalUser left join fetch normalUser.participatedSurveys where normalUser.id = :id",
                NormalUser.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<NormalUser> fetchParticipatedSurveys(List<NormalUser> normalUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, normalUsers.size()).forEach(index -> order.put(normalUsers.get(index).getId(), index));
        List<NormalUser> result = entityManager
            .createQuery(
                "select normalUser from NormalUser normalUser left join fetch normalUser.participatedSurveys where normalUser in :normalUsers",
                NormalUser.class
            )
            .setParameter("normalUsers", normalUsers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
