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
import xyz.jhmapstruct.domain.NextSupplierSigma;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierSigmaRepositoryWithBagRelationshipsImpl implements NextSupplierSigmaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERSIGMAS_PARAMETER = "nextSupplierSigmas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierSigma> fetchBagRelationships(Optional<NextSupplierSigma> nextSupplierSigma) {
        return nextSupplierSigma.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierSigma> fetchBagRelationships(Page<NextSupplierSigma> nextSupplierSigmas) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierSigmas.getContent()),
            nextSupplierSigmas.getPageable(),
            nextSupplierSigmas.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierSigma> fetchBagRelationships(List<NextSupplierSigma> nextSupplierSigmas) {
        return Optional.of(nextSupplierSigmas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierSigma fetchProducts(NextSupplierSigma result) {
        return entityManager
            .createQuery(
                "select nextSupplierSigma from NextSupplierSigma nextSupplierSigma left join fetch nextSupplierSigma.products where nextSupplierSigma.id = :id",
                NextSupplierSigma.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierSigma> fetchProducts(List<NextSupplierSigma> nextSupplierSigmas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierSigmas.size()).forEach(index -> order.put(nextSupplierSigmas.get(index).getId(), index));
        List<NextSupplierSigma> result = entityManager
            .createQuery(
                "select nextSupplierSigma from NextSupplierSigma nextSupplierSigma left join fetch nextSupplierSigma.products where nextSupplierSigma in :nextSupplierSigmas",
                NextSupplierSigma.class
            )
            .setParameter(NEXTSUPPLIERSIGMAS_PARAMETER, nextSupplierSigmas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
