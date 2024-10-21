package ai.realworld.repository;

import ai.realworld.domain.AlProPro;
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
public class AlProProRepositoryWithBagRelationshipsImpl implements AlProProRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALPROPROS_PARAMETER = "alProPros";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlProPro> fetchBagRelationships(Optional<AlProPro> alProPro) {
        return alProPro.map(this::fetchAmenities).map(this::fetchImages);
    }

    @Override
    public Page<AlProPro> fetchBagRelationships(Page<AlProPro> alProPros) {
        return new PageImpl<>(fetchBagRelationships(alProPros.getContent()), alProPros.getPageable(), alProPros.getTotalElements());
    }

    @Override
    public List<AlProPro> fetchBagRelationships(List<AlProPro> alProPros) {
        return Optional.of(alProPros).map(this::fetchAmenities).map(this::fetchImages).orElse(Collections.emptyList());
    }

    AlProPro fetchAmenities(AlProPro result) {
        return entityManager
            .createQuery(
                "select alProPro from AlProPro alProPro left join fetch alProPro.amenities where alProPro.id = :id",
                AlProPro.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlProPro> fetchAmenities(List<AlProPro> alProPros) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alProPros.size()).forEach(index -> order.put(alProPros.get(index).getId(), index));
        List<AlProPro> result = entityManager
            .createQuery(
                "select alProPro from AlProPro alProPro left join fetch alProPro.amenities where alProPro in :alProPros",
                AlProPro.class
            )
            .setParameter(ALPROPROS_PARAMETER, alProPros)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    AlProPro fetchImages(AlProPro result) {
        return entityManager
            .createQuery("select alProPro from AlProPro alProPro left join fetch alProPro.images where alProPro.id = :id", AlProPro.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlProPro> fetchImages(List<AlProPro> alProPros) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alProPros.size()).forEach(index -> order.put(alProPros.get(index).getId(), index));
        List<AlProPro> result = entityManager
            .createQuery(
                "select alProPro from AlProPro alProPro left join fetch alProPro.images where alProPro in :alProPros",
                AlProPro.class
            )
            .setParameter(ALPROPROS_PARAMETER, alProPros)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
