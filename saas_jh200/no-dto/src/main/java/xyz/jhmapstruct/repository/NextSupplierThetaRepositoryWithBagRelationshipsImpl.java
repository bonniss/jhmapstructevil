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
import xyz.jhmapstruct.domain.NextSupplierTheta;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierThetaRepositoryWithBagRelationshipsImpl implements NextSupplierThetaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERTHETAS_PARAMETER = "nextSupplierThetas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierTheta> fetchBagRelationships(Optional<NextSupplierTheta> nextSupplierTheta) {
        return nextSupplierTheta.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierTheta> fetchBagRelationships(Page<NextSupplierTheta> nextSupplierThetas) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierThetas.getContent()),
            nextSupplierThetas.getPageable(),
            nextSupplierThetas.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierTheta> fetchBagRelationships(List<NextSupplierTheta> nextSupplierThetas) {
        return Optional.of(nextSupplierThetas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierTheta fetchProducts(NextSupplierTheta result) {
        return entityManager
            .createQuery(
                "select nextSupplierTheta from NextSupplierTheta nextSupplierTheta left join fetch nextSupplierTheta.products where nextSupplierTheta.id = :id",
                NextSupplierTheta.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierTheta> fetchProducts(List<NextSupplierTheta> nextSupplierThetas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierThetas.size()).forEach(index -> order.put(nextSupplierThetas.get(index).getId(), index));
        List<NextSupplierTheta> result = entityManager
            .createQuery(
                "select nextSupplierTheta from NextSupplierTheta nextSupplierTheta left join fetch nextSupplierTheta.products where nextSupplierTheta in :nextSupplierThetas",
                NextSupplierTheta.class
            )
            .setParameter(NEXTSUPPLIERTHETAS_PARAMETER, nextSupplierThetas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
