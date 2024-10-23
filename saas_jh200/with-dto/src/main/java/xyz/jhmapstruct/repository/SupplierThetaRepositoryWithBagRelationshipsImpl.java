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
import xyz.jhmapstruct.domain.SupplierTheta;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierThetaRepositoryWithBagRelationshipsImpl implements SupplierThetaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SUPPLIERTHETAS_PARAMETER = "supplierThetas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SupplierTheta> fetchBagRelationships(Optional<SupplierTheta> supplierTheta) {
        return supplierTheta.map(this::fetchProducts);
    }

    @Override
    public Page<SupplierTheta> fetchBagRelationships(Page<SupplierTheta> supplierThetas) {
        return new PageImpl<>(
            fetchBagRelationships(supplierThetas.getContent()),
            supplierThetas.getPageable(),
            supplierThetas.getTotalElements()
        );
    }

    @Override
    public List<SupplierTheta> fetchBagRelationships(List<SupplierTheta> supplierThetas) {
        return Optional.of(supplierThetas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    SupplierTheta fetchProducts(SupplierTheta result) {
        return entityManager
            .createQuery(
                "select supplierTheta from SupplierTheta supplierTheta left join fetch supplierTheta.products where supplierTheta.id = :id",
                SupplierTheta.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SupplierTheta> fetchProducts(List<SupplierTheta> supplierThetas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, supplierThetas.size()).forEach(index -> order.put(supplierThetas.get(index).getId(), index));
        List<SupplierTheta> result = entityManager
            .createQuery(
                "select supplierTheta from SupplierTheta supplierTheta left join fetch supplierTheta.products where supplierTheta in :supplierThetas",
                SupplierTheta.class
            )
            .setParameter(SUPPLIERTHETAS_PARAMETER, supplierThetas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
