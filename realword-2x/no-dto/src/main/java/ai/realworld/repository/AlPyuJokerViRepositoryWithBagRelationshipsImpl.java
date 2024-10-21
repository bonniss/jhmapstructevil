package ai.realworld.repository;

import ai.realworld.domain.AlPyuJokerVi;
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
public class AlPyuJokerViRepositoryWithBagRelationshipsImpl implements AlPyuJokerViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALPYUJOKERVIS_PARAMETER = "alPyuJokerVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlPyuJokerVi> fetchBagRelationships(Optional<AlPyuJokerVi> alPyuJokerVi) {
        return alPyuJokerVi.map(this::fetchProperties);
    }

    @Override
    public Page<AlPyuJokerVi> fetchBagRelationships(Page<AlPyuJokerVi> alPyuJokerVis) {
        return new PageImpl<>(
            fetchBagRelationships(alPyuJokerVis.getContent()),
            alPyuJokerVis.getPageable(),
            alPyuJokerVis.getTotalElements()
        );
    }

    @Override
    public List<AlPyuJokerVi> fetchBagRelationships(List<AlPyuJokerVi> alPyuJokerVis) {
        return Optional.of(alPyuJokerVis).map(this::fetchProperties).orElse(Collections.emptyList());
    }

    AlPyuJokerVi fetchProperties(AlPyuJokerVi result) {
        return entityManager
            .createQuery(
                "select alPyuJokerVi from AlPyuJokerVi alPyuJokerVi left join fetch alPyuJokerVi.properties where alPyuJokerVi.id = :id",
                AlPyuJokerVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlPyuJokerVi> fetchProperties(List<AlPyuJokerVi> alPyuJokerVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alPyuJokerVis.size()).forEach(index -> order.put(alPyuJokerVis.get(index).getId(), index));
        List<AlPyuJokerVi> result = entityManager
            .createQuery(
                "select alPyuJokerVi from AlPyuJokerVi alPyuJokerVi left join fetch alPyuJokerVi.properties where alPyuJokerVi in :alPyuJokerVis",
                AlPyuJokerVi.class
            )
            .setParameter(ALPYUJOKERVIS_PARAMETER, alPyuJokerVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
