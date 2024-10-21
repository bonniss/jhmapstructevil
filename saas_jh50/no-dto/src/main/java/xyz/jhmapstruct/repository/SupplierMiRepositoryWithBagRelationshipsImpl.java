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
import xyz.jhmapstruct.domain.SupplierMi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierMiRepositoryWithBagRelationshipsImpl implements SupplierMiRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERMIS_PARAMETER = "supplierMis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierMi> fetchBagRelationships(Optional<SupplierMi> supplierMi) {
        return supplierMi.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierMi> fetchBagRelationships(Page<SupplierMi> supplierMis) {
        return new PageImpl<>(fetchBagRelationships(supplierMis.getContent()), supplierMis.getPageable(), supplierMis.getTotalElements());
    }

    @Override
    public List<SupplierMi> fetchBagRelationships(List<SupplierMi> supplierMis) {
        return Optional.of(supplierMis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierMi fetchProducts(SupplierMi result) {
        return entityManager
            .createQuery(
                "select supplierMi from SupplierMi supplierMi left join fetch supplierMi.products where supplierMi.id = :id",
                SupplierMi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierMi> fetchProducts(List<SupplierMi> supplierMis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierMis.size()).forEach(index -> order.put(supplierMis.get(index).getId(), index));
        List<SupplierMi> result = entityManager
            .createQuery(
                "select supplierMi from SupplierMi supplierMi left join fetch supplierMi.products where supplierMi in :supplierMis",
                SupplierMi.class
            )
            .setParameter(SUPPLIERMIS_PARAMETER, supplierMis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
