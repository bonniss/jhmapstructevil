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
import xyz.jhmapstruct.domain.NextSupplierBeta;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierBetaRepositoryWithBagRelationshipsImpl implements NextSupplierBetaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERBETAS_PARAMETER = "nextSupplierBetas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierBeta> fetchBagRelationships(Optional<NextSupplierBeta> nextSupplierBeta) {
        return nextSupplierBeta.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierBeta> fetchBagRelationships(Page<NextSupplierBeta> nextSupplierBetas) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierBetas.getContent()),
            nextSupplierBetas.getPageable(),
            nextSupplierBetas.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierBeta> fetchBagRelationships(List<NextSupplierBeta> nextSupplierBetas) {
        return Optional.of(nextSupplierBetas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierBeta fetchProducts(NextSupplierBeta result) {
        return entityManager
            .createQuery(
                "select nextSupplierBeta from NextSupplierBeta nextSupplierBeta left join fetch nextSupplierBeta.products where nextSupplierBeta.id = :id",
                NextSupplierBeta.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierBeta> fetchProducts(List<NextSupplierBeta> nextSupplierBetas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierBetas.size()).forEach(index -> order.put(nextSupplierBetas.get(index).getId(), index));
        List<NextSupplierBeta> result = entityManager
            .createQuery(
                "select nextSupplierBeta from NextSupplierBeta nextSupplierBeta left join fetch nextSupplierBeta.products where nextSupplierBeta in :nextSupplierBetas",
                NextSupplierBeta.class
            )
            .setParameter(NEXTSUPPLIERBETAS_PARAMETER, nextSupplierBetas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
