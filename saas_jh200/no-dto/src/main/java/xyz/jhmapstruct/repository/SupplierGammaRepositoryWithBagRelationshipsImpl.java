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
import xyz.jhmapstruct.domain.SupplierGamma;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierGammaRepositoryWithBagRelationshipsImpl implements SupplierGammaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERGAMMAS_PARAMETER = "supplierGammas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierGamma> fetchBagRelationships(Optional<SupplierGamma> supplierGamma) {
        return supplierGamma.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierGamma> fetchBagRelationships(Page<SupplierGamma> supplierGammas) {
        return new PageImpl<>(
            fetchBagRelationships(supplierGammas.getContent()),
            supplierGammas.getPageable(),
            supplierGammas.getTotalElements()
        );
    }

    @Override
    public List<SupplierGamma> fetchBagRelationships(List<SupplierGamma> supplierGammas) {
        return Optional.of(supplierGammas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierGamma fetchProducts(SupplierGamma result) {
        return entityManager
            .createQuery(
                "select supplierGamma from SupplierGamma supplierGamma left join fetch supplierGamma.products where supplierGamma.id = :id",
                SupplierGamma.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierGamma> fetchProducts(List<SupplierGamma> supplierGammas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierGammas.size()).forEach(index -> order.put(supplierGammas.get(index).getId(), index));
        List<SupplierGamma> result = entityManager
            .createQuery(
                "select supplierGamma from SupplierGamma supplierGamma left join fetch supplierGamma.products where supplierGamma in :supplierGammas",
                SupplierGamma.class
            )
            .setParameter(SUPPLIERGAMMAS_PARAMETER, supplierGammas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
