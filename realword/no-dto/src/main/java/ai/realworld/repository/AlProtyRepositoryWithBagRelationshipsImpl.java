package ai.realworld.repository;

import ai.realworld.domain.AlProty;
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
public class AlProtyRepositoryWithBagRelationshipsImpl implements AlProtyRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ALPROTIES_PARAMETER = "alProties";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AlProty> fetchBagRelationships(Optional<AlProty> alProty) {
        return alProty.map(this::fetchImages);
    }

    @Override
    public Page<AlProty> fetchBagRelationships(Page<AlProty> alProties) {
        return new PageImpl<>(fetchBagRelationships(alProties.getContent()), alProties.getPageable(), alProties.getTotalElements());
    }

    @Override
    public List<AlProty> fetchBagRelationships(List<AlProty> alProties) {
        return Optional.of(alProties).map(this::fetchImages).orElse(Collections.emptyList());
    }

    AlProty fetchImages(AlProty result) {
        return entityManager
            .createQuery("select alProty from AlProty alProty left join fetch alProty.images where alProty.id = :id", AlProty.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AlProty> fetchImages(List<AlProty> alProties) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, alProties.size()).forEach(index -> order.put(alProties.get(index).getId(), index));
        List<AlProty> result = entityManager
            .createQuery("select alProty from AlProty alProty left join fetch alProty.images where alProty in :alProties", AlProty.class)
            .setParameter(ALPROTIES_PARAMETER, alProties)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
