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
import xyz.jhmapstruct.domain.SupplierMiMi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierMiMiRepositoryWithBagRelationshipsImpl implements SupplierMiMiRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERMIMIS_PARAMETER = "supplierMiMis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierMiMi> fetchBagRelationships(Optional<SupplierMiMi> supplierMiMi) {
        return supplierMiMi.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierMiMi> fetchBagRelationships(Page<SupplierMiMi> supplierMiMis) {
        return new PageImpl<>(
            fetchBagRelationships(supplierMiMis.getContent()),
            supplierMiMis.getPageable(),
            supplierMiMis.getTotalElements()
        );
    }

    @Override
    public List<SupplierMiMi> fetchBagRelationships(List<SupplierMiMi> supplierMiMis) {
        return Optional.of(supplierMiMis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierMiMi fetchProducts(SupplierMiMi result) {
        return entityManager
            .createQuery(
                "select supplierMiMi from SupplierMiMi supplierMiMi left join fetch supplierMiMi.products where supplierMiMi.id = :id",
                SupplierMiMi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierMiMi> fetchProducts(List<SupplierMiMi> supplierMiMis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierMiMis.size()).forEach(index -> order.put(supplierMiMis.get(index).getId(), index));
        List<SupplierMiMi> result = entityManager
            .createQuery(
                "select supplierMiMi from SupplierMiMi supplierMiMi left join fetch supplierMiMi.products where supplierMiMi in :supplierMiMis",
                SupplierMiMi.class
            )
            .setParameter(SUPPLIERMIMIS_PARAMETER, supplierMiMis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
