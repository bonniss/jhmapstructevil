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
import xyz.jhmapstruct.domain.SupplierBeta;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierBetaRepositoryWithBagRelationshipsImpl implements SupplierBetaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERBETAS_PARAMETER = "supplierBetas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierBeta> fetchBagRelationships(Optional<SupplierBeta> supplierBeta) {
        return supplierBeta.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierBeta> fetchBagRelationships(Page<SupplierBeta> supplierBetas) {
        return new PageImpl<>(
            fetchBagRelationships(supplierBetas.getContent()),
            supplierBetas.getPageable(),
            supplierBetas.getTotalElements()
        );
    }

    @Override
    public List<SupplierBeta> fetchBagRelationships(List<SupplierBeta> supplierBetas) {
        return Optional.of(supplierBetas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierBeta fetchProducts(SupplierBeta result) {
        return entityManager
            .createQuery(
                "select supplierBeta from SupplierBeta supplierBeta left join fetch supplierBeta.products where supplierBeta.id = :id",
                SupplierBeta.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierBeta> fetchProducts(List<SupplierBeta> supplierBetas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierBetas.size()).forEach(index -> order.put(supplierBetas.get(index).getId(), index));
        List<SupplierBeta> result = entityManager
            .createQuery(
                "select supplierBeta from SupplierBeta supplierBeta left join fetch supplierBeta.products where supplierBeta in :supplierBetas",
                SupplierBeta.class
            )
            .setParameter(SUPPLIERBETAS_PARAMETER, supplierBetas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
