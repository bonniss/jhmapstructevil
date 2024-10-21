package ai.realworld.repository;

import ai.realworld.domain.AlProtyVi;
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
public class AlProtyViRepositoryWithBagRelationshipsImpl implements AlProtyViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALPROTYVIS_PARAMETER = "alProtyVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlProtyVi> fetchBagRelationships(Optional<AlProtyVi> alProtyVi) {
        return alProtyVi.map(this::fetchImages);
    }

    @Override
    public Page<AlProtyVi> fetchBagRelationships(Page<AlProtyVi> alProtyVis) {
        return new PageImpl<>(fetchBagRelationships(alProtyVis.getContent()), alProtyVis.getPageable(), alProtyVis.getTotalElements());
    }

    @Override
    public List<AlProtyVi> fetchBagRelationships(List<AlProtyVi> alProtyVis) {
        return Optional.of(alProtyVis).map(this::fetchImages).orElse(Collections.emptyList());
    }

    AlProtyVi fetchImages(AlProtyVi result) {
        return entityManager
            .createQuery(
                "select alProtyVi from AlProtyVi alProtyVi left join fetch alProtyVi.images where alProtyVi.id = :id",
                AlProtyVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlProtyVi> fetchImages(List<AlProtyVi> alProtyVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alProtyVis.size()).forEach(index -> order.put(alProtyVis.get(index).getId(), index));
        List<AlProtyVi> result = entityManager
            .createQuery(
                "select alProtyVi from AlProtyVi alProtyVi left join fetch alProtyVi.images where alProtyVi in :alProtyVis",
                AlProtyVi.class
            )
            .setParameter(ALPROTYVIS_PARAMETER, alProtyVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
