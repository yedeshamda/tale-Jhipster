package com.tale.repository;

import com.tale.domain.Survey;
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
public class SurveyRepositoryWithBagRelationshipsImpl implements SurveyRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Survey> fetchBagRelationships(Optional<Survey> survey) {
        return survey.map(this::fetchParticipants);
    }

    @Override
    public Page<Survey> fetchBagRelationships(Page<Survey> surveys) {
        return new PageImpl<>(fetchBagRelationships(surveys.getContent()), surveys.getPageable(), surveys.getTotalElements());
    }

    @Override
    public List<Survey> fetchBagRelationships(List<Survey> surveys) {
        return Optional.of(surveys).map(this::fetchParticipants).orElse(Collections.emptyList());
    }

    Survey fetchParticipants(Survey result) {
        return entityManager
            .createQuery("select survey from Survey survey left join fetch survey.participants where survey.id = :id", Survey.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Survey> fetchParticipants(List<Survey> surveys) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, surveys.size()).forEach(index -> order.put(surveys.get(index).getId(), index));
        List<Survey> result = entityManager
            .createQuery("select survey from Survey survey left join fetch survey.participants where survey in :surveys", Survey.class)
            .setParameter("surveys", surveys)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
