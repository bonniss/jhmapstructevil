package xyz.jhmapstruct.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import xyz.jhmapstruct.domain.NextSupplierMiMi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierMiMiRepositoryWithBagRelationshipsImpl implements NextSupplierMiMiRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERMIMIS_PARAMETER = "nextSupplierMiMis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierMiMi> fetchBagRelationships(Optional<NextSupplierMiMi> nextSupplierMiMi) {
        return nextSupplierMiMi.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierMiMi> fetchBagRelationships(Page<NextSupplierMiMi> nextSupplierMiMis) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierMiMis.getContent()),
            nextSupplierMiMis.getPageable(),
            nextSupplierMiMis.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierMiMi> fetchBagRelationships(List<NextSupplierMiMi> nextSupplierMiMis) {
        return Optional.of(nextSupplierMiMis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierMiMi fetchProducts(NextSupplierMiMi result) {
        return entityManager
            .createQuery(
                "select nextSupplierMiMi from NextSupplierMiMi nextSupplierMiMi left join fetch nextSupplierMiMi.products where nextSupplierMiMi.id = :id",
                NextSupplierMiMi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierMiMi> fetchProducts(List<NextSupplierMiMi> nextSupplierMiMis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierMiMis.size()).forEach(index -> order.put(nextSupplierMiMis.get(index).getId(), index));
        List<NextSupplierMiMi> result = entityManager
            .createQuery(
                "select nextSupplierMiMi from NextSupplierMiMi nextSupplierMiMi left join fetch nextSupplierMiMi.products where nextSupplierMiMi in :nextSupplierMiMis",
                NextSupplierMiMi.class
            )
            .setParameter(NEXTSUPPLIERMIMIS_PARAMETER, nextSupplierMiMis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
