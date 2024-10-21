package ai.realworld.repository;

import ai.realworld.domain.AlPyuJoker;
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
public class AlPyuJokerRepositoryWithBagRelationshipsImpl implements AlPyuJokerRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALPYUJOKERS_PARAMETER = "alPyuJokers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlPyuJoker> fetchBagRelationships(Optional<AlPyuJoker> alPyuJoker) {
        return alPyuJoker.map(this::fetchProperties);
    }

    @Override
    public Page<AlPyuJoker> fetchBagRelationships(Page<AlPyuJoker> alPyuJokers) {
        return new PageImpl<>(fetchBagRelationships(alPyuJokers.getContent()), alPyuJokers.getPageable(), alPyuJokers.getTotalElements());
    }

    @Override
    public List<AlPyuJoker> fetchBagRelationships(List<AlPyuJoker> alPyuJokers) {
        return Optional.of(alPyuJokers).map(this::fetchProperties).orElse(Collections.emptyList());
    }

    AlPyuJoker fetchProperties(AlPyuJoker result) {
        return entityManager
            .createQuery(
                "select alPyuJoker from AlPyuJoker alPyuJoker left join fetch alPyuJoker.properties where alPyuJoker.id = :id",
                AlPyuJoker.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlPyuJoker> fetchProperties(List<AlPyuJoker> alPyuJokers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alPyuJokers.size()).forEach(index -> order.put(alPyuJokers.get(index).getId(), index));
        List<AlPyuJoker> result = entityManager
            .createQuery(
                "select alPyuJoker from AlPyuJoker alPyuJoker left join fetch alPyuJoker.properties where alPyuJoker in :alPyuJokers",
                AlPyuJoker.class
            )
            .setParameter(ALPYUJOKERS_PARAMETER, alPyuJokers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
