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
import xyz.jhmapstruct.domain.NextSupplierViVi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierViViRepositoryWithBagRelationshipsImpl implements NextSupplierViViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERVIVIS_PARAMETER = "nextSupplierViVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierViVi> fetchBagRelationships(Optional<NextSupplierViVi> nextSupplierViVi) {
        return nextSupplierViVi.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierViVi> fetchBagRelationships(Page<NextSupplierViVi> nextSupplierViVis) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierViVis.getContent()),
            nextSupplierViVis.getPageable(),
            nextSupplierViVis.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierViVi> fetchBagRelationships(List<NextSupplierViVi> nextSupplierViVis) {
        return Optional.of(nextSupplierViVis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierViVi fetchProducts(NextSupplierViVi result) {
        return entityManager
            .createQuery(
                "select nextSupplierViVi from NextSupplierViVi nextSupplierViVi left join fetch nextSupplierViVi.products where nextSupplierViVi.id = :id",
                NextSupplierViVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierViVi> fetchProducts(List<NextSupplierViVi> nextSupplierViVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierViVis.size()).forEach(index -> order.put(nextSupplierViVis.get(index).getId(), index));
        List<NextSupplierViVi> result = entityManager
            .createQuery(
                "select nextSupplierViVi from NextSupplierViVi nextSupplierViVi left join fetch nextSupplierViVi.products where nextSupplierViVi in :nextSupplierViVis",
                NextSupplierViVi.class
            )
            .setParameter(NEXTSUPPLIERVIVIS_PARAMETER, nextSupplierViVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
