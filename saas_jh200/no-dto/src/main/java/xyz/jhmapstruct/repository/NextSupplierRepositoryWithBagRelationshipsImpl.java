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
import xyz.jhmapstruct.domain.NextSupplier;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierRepositoryWithBagRelationshipsImpl implements NextSupplierRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERS_PARAMETER = "nextSuppliers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplier> fetchBagRelationships(Optional<NextSupplier> nextSupplier) {
        return nextSupplier.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplier> fetchBagRelationships(Page<NextSupplier> nextSuppliers) {
        return new PageImpl<>(
            fetchBagRelationships(nextSuppliers.getContent()),
            nextSuppliers.getPageable(),
            nextSuppliers.getTotalElements()
        );
    }

    @Override
    public List<NextSupplier> fetchBagRelationships(List<NextSupplier> nextSuppliers) {
        return Optional.of(nextSuppliers).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplier fetchProducts(NextSupplier result) {
        return entityManager
            .createQuery(
                "select nextSupplier from NextSupplier nextSupplier left join fetch nextSupplier.products where nextSupplier.id = :id",
                NextSupplier.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplier> fetchProducts(List<NextSupplier> nextSuppliers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSuppliers.size()).forEach(index -> order.put(nextSuppliers.get(index).getId(), index));
        List<NextSupplier> result = entityManager
            .createQuery(
                "select nextSupplier from NextSupplier nextSupplier left join fetch nextSupplier.products where nextSupplier in :nextSuppliers",
                NextSupplier.class
            )
            .setParameter(NEXTSUPPLIERS_PARAMETER, nextSuppliers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
