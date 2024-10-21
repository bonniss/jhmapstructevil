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
import xyz.jhmapstruct.domain.SupplierVi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierViRepositoryWithBagRelationshipsImpl implements SupplierViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERVIS_PARAMETER = "supplierVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierVi> fetchBagRelationships(Optional<SupplierVi> supplierVi) {
        return supplierVi.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierVi> fetchBagRelationships(Page<SupplierVi> supplierVis) {
        return new PageImpl<>(fetchBagRelationships(supplierVis.getContent()), supplierVis.getPageable(), supplierVis.getTotalElements());
    }

    @Override
    public List<SupplierVi> fetchBagRelationships(List<SupplierVi> supplierVis) {
        return Optional.of(supplierVis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierVi fetchProducts(SupplierVi result) {
        return entityManager
            .createQuery(
                "select supplierVi from SupplierVi supplierVi left join fetch supplierVi.products where supplierVi.id = :id",
                SupplierVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierVi> fetchProducts(List<SupplierVi> supplierVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierVis.size()).forEach(index -> order.put(supplierVis.get(index).getId(), index));
        List<SupplierVi> result = entityManager
            .createQuery(
                "select supplierVi from SupplierVi supplierVi left join fetch supplierVi.products where supplierVi in :supplierVis",
                SupplierVi.class
            )
            .setParameter(SUPPLIERVIS_PARAMETER, supplierVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
