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
import xyz.jhmapstruct.domain.NextSupplierMi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierMiRepositoryWithBagRelationshipsImpl implements NextSupplierMiRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERMIS_PARAMETER = "nextSupplierMis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierMi> fetchBagRelationships(Optional<NextSupplierMi> nextSupplierMi) {
        return nextSupplierMi.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierMi> fetchBagRelationships(Page<NextSupplierMi> nextSupplierMis) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierMis.getContent()),
            nextSupplierMis.getPageable(),
            nextSupplierMis.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierMi> fetchBagRelationships(List<NextSupplierMi> nextSupplierMis) {
        return Optional.of(nextSupplierMis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierMi fetchProducts(NextSupplierMi result) {
        return entityManager
            .createQuery(
                "select nextSupplierMi from NextSupplierMi nextSupplierMi left join fetch nextSupplierMi.products where nextSupplierMi.id = :id",
                NextSupplierMi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierMi> fetchProducts(List<NextSupplierMi> nextSupplierMis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierMis.size()).forEach(index -> order.put(nextSupplierMis.get(index).getId(), index));
        List<NextSupplierMi> result = entityManager
            .createQuery(
                "select nextSupplierMi from NextSupplierMi nextSupplierMi left join fetch nextSupplierMi.products where nextSupplierMi in :nextSupplierMis",
                NextSupplierMi.class
            )
            .setParameter(NEXTSUPPLIERMIS_PARAMETER, nextSupplierMis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
