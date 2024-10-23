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
import xyz.jhmapstruct.domain.SupplierAlpha;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierAlphaRepositoryWithBagRelationshipsImpl implements SupplierAlphaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERALPHAS_PARAMETER = "supplierAlphas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierAlpha> fetchBagRelationships(Optional<SupplierAlpha> supplierAlpha) {
        return supplierAlpha.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierAlpha> fetchBagRelationships(Page<SupplierAlpha> supplierAlphas) {
        return new PageImpl<>(
            fetchBagRelationships(supplierAlphas.getContent()),
            supplierAlphas.getPageable(),
            supplierAlphas.getTotalElements()
        );
    }

    @Override
    public List<SupplierAlpha> fetchBagRelationships(List<SupplierAlpha> supplierAlphas) {
        return Optional.of(supplierAlphas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierAlpha fetchProducts(SupplierAlpha result) {
        return entityManager
            .createQuery(
                "select supplierAlpha from SupplierAlpha supplierAlpha left join fetch supplierAlpha.products where supplierAlpha.id = :id",
                SupplierAlpha.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierAlpha> fetchProducts(List<SupplierAlpha> supplierAlphas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierAlphas.size()).forEach(index -> order.put(supplierAlphas.get(index).getId(), index));
        List<SupplierAlpha> result = entityManager
            .createQuery(
                "select supplierAlpha from SupplierAlpha supplierAlpha left join fetch supplierAlpha.products where supplierAlpha in :supplierAlphas",
                SupplierAlpha.class
            )
            .setParameter(SUPPLIERALPHAS_PARAMETER, supplierAlphas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
