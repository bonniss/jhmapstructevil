package ai.realworld.repository;

import ai.realworld.domain.AlProProVi;
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
public class AlProProViRepositoryWithBagRelationshipsImpl implements AlProProViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALPROPROVIS_PARAMETER = "alProProVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlProProVi> fetchBagRelationships(Optional<AlProProVi> alProProVi) {
        return alProProVi.map(this::fetchAmenities).map(this::fetchImages);
    }

    @Override
    public Page<AlProProVi> fetchBagRelationships(Page<AlProProVi> alProProVis) {
        return new PageImpl<>(fetchBagRelationships(alProProVis.getContent()), alProProVis.getPageable(), alProProVis.getTotalElements());
    }

    @Override
    public List<AlProProVi> fetchBagRelationships(List<AlProProVi> alProProVis) {
        return Optional.of(alProProVis).map(this::fetchAmenities).map(this::fetchImages).orElse(Collections.emptyList());
    }

    AlProProVi fetchAmenities(AlProProVi result) {
        return entityManager
            .createQuery(
                "select alProProVi from AlProProVi alProProVi left join fetch alProProVi.amenities where alProProVi.id = :id",
                AlProProVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlProProVi> fetchAmenities(List<AlProProVi> alProProVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alProProVis.size()).forEach(index -> order.put(alProProVis.get(index).getId(), index));
        List<AlProProVi> result = entityManager
            .createQuery(
                "select alProProVi from AlProProVi alProProVi left join fetch alProProVi.amenities where alProProVi in :alProProVis",
                AlProProVi.class
            )
            .setParameter(ALPROPROVIS_PARAMETER, alProProVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    AlProProVi fetchImages(AlProProVi result) {
        return entityManager
            .createQuery(
                "select alProProVi from AlProProVi alProProVi left join fetch alProProVi.images where alProProVi.id = :id",
                AlProProVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlProProVi> fetchImages(List<AlProProVi> alProProVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alProProVis.size()).forEach(index -> order.put(alProProVis.get(index).getId(), index));
        List<AlProProVi> result = entityManager
            .createQuery(
                "select alProProVi from AlProProVi alProProVi left join fetch alProProVi.images where alProProVi in :alProProVis",
                AlProProVi.class
            )
            .setParameter(ALPROPROVIS_PARAMETER, alProProVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
