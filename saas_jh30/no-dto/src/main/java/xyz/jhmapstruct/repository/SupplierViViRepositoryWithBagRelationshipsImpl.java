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
import xyz.jhmapstruct.domain.SupplierViVi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierViViRepositoryWithBagRelationshipsImpl implements SupplierViViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERVIVIS_PARAMETER = "supplierViVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierViVi> fetchBagRelationships(Optional<SupplierViVi> supplierViVi) {
        return supplierViVi.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierViVi> fetchBagRelationships(Page<SupplierViVi> supplierViVis) {
        return new PageImpl<>(
            fetchBagRelationships(supplierViVis.getContent()),
            supplierViVis.getPageable(),
            supplierViVis.getTotalElements()
        );
    }

    @Override
    public List<SupplierViVi> fetchBagRelationships(List<SupplierViVi> supplierViVis) {
        return Optional.of(supplierViVis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierViVi fetchProducts(SupplierViVi result) {
        return entityManager
            .createQuery(
                "select supplierViVi from SupplierViVi supplierViVi left join fetch supplierViVi.products where supplierViVi.id = :id",
                SupplierViVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierViVi> fetchProducts(List<SupplierViVi> supplierViVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierViVis.size()).forEach(index -> order.put(supplierViVis.get(index).getId(), index));
        List<SupplierViVi> result = entityManager
            .createQuery(
                "select supplierViVi from SupplierViVi supplierViVi left join fetch supplierViVi.products where supplierViVi in :supplierViVis",
                SupplierViVi.class
            )
            .setParameter(SUPPLIERVIVIS_PARAMETER, supplierViVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
