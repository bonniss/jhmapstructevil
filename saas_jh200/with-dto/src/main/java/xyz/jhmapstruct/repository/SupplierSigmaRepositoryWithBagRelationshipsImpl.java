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
import xyz.jhmapstruct.domain.SupplierSigma;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierSigmaRepositoryWithBagRelationshipsImpl implements SupplierSigmaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERSIGMAS_PARAMETER = "supplierSigmas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierSigma> fetchBagRelationships(Optional<SupplierSigma> supplierSigma) {
        return supplierSigma.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierSigma> fetchBagRelationships(Page<SupplierSigma> supplierSigmas) {
        return new PageImpl<>(
            fetchBagRelationships(supplierSigmas.getContent()),
            supplierSigmas.getPageable(),
            supplierSigmas.getTotalElements()
        );
    }

    @Override
    public List<SupplierSigma> fetchBagRelationships(List<SupplierSigma> supplierSigmas) {
        return Optional.of(supplierSigmas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierSigma fetchProducts(SupplierSigma result) {
        return entityManager
            .createQuery(
                "select supplierSigma from SupplierSigma supplierSigma left join fetch supplierSigma.products where supplierSigma.id = :id",
                SupplierSigma.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierSigma> fetchProducts(List<SupplierSigma> supplierSigmas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierSigmas.size()).forEach(index -> order.put(supplierSigmas.get(index).getId(), index));
        List<SupplierSigma> result = entityManager
            .createQuery(
                "select supplierSigma from SupplierSigma supplierSigma left join fetch supplierSigma.products where supplierSigma in :supplierSigmas",
                SupplierSigma.class
            )
            .setParameter(SUPPLIERSIGMAS_PARAMETER, supplierSigmas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
