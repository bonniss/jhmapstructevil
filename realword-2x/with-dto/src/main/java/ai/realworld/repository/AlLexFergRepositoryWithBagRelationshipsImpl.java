package ai.realworld.repository;

import ai.realworld.domain.AlLexFerg;
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
public class AlLexFergRepositoryWithBagRelationshipsImpl implements AlLexFergRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALLEXFERGS_PARAMETER = "alLexFergs";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlLexFerg> fetchBagRelationships(Optional<AlLexFerg> alLexFerg) {
        return alLexFerg.map(this::fetchTags);
    }

    @Override
    public Page<AlLexFerg> fetchBagRelationships(Page<AlLexFerg> alLexFergs) {
        return new PageImpl<>(fetchBagRelationships(alLexFergs.getContent()), alLexFergs.getPageable(), alLexFergs.getTotalElements());
    }

    @Override
    public List<AlLexFerg> fetchBagRelationships(List<AlLexFerg> alLexFergs) {
        return Optional.of(alLexFergs).map(this::fetchTags).orElse(Collections.emptyList());
    }

    AlLexFerg fetchTags(AlLexFerg result) {
        return entityManager
            .createQuery(
                "select alLexFerg from AlLexFerg alLexFerg left join fetch alLexFerg.tags where alLexFerg.id = :id",
                AlLexFerg.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlLexFerg> fetchTags(List<AlLexFerg> alLexFergs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alLexFergs.size()).forEach(index -> order.put(alLexFergs.get(index).getId(), index));
        List<AlLexFerg> result = entityManager
            .createQuery(
                "select alLexFerg from AlLexFerg alLexFerg left join fetch alLexFerg.tags where alLexFerg in :alLexFergs",
                AlLexFerg.class
            )
            .setParameter(ALLEXFERGS_PARAMETER, alLexFergs)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
