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
import xyz.jhmapstruct.domain.NextSupplierGamma;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierGammaRepositoryWithBagRelationshipsImpl implements NextSupplierGammaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERGAMMAS_PARAMETER = "nextSupplierGammas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierGamma> fetchBagRelationships(Optional<NextSupplierGamma> nextSupplierGamma) {
        return nextSupplierGamma.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierGamma> fetchBagRelationships(Page<NextSupplierGamma> nextSupplierGammas) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierGammas.getContent()),
            nextSupplierGammas.getPageable(),
            nextSupplierGammas.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierGamma> fetchBagRelationships(List<NextSupplierGamma> nextSupplierGammas) {
        return Optional.of(nextSupplierGammas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierGamma fetchProducts(NextSupplierGamma result) {
        return entityManager
            .createQuery(
                "select nextSupplierGamma from NextSupplierGamma nextSupplierGamma left join fetch nextSupplierGamma.products where nextSupplierGamma.id = :id",
                NextSupplierGamma.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierGamma> fetchProducts(List<NextSupplierGamma> nextSupplierGammas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierGammas.size()).forEach(index -> order.put(nextSupplierGammas.get(index).getId(), index));
        List<NextSupplierGamma> result = entityManager
            .createQuery(
                "select nextSupplierGamma from NextSupplierGamma nextSupplierGamma left join fetch nextSupplierGamma.products where nextSupplierGamma in :nextSupplierGammas",
                NextSupplierGamma.class
            )
            .setParameter(NEXTSUPPLIERGAMMAS_PARAMETER, nextSupplierGammas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
