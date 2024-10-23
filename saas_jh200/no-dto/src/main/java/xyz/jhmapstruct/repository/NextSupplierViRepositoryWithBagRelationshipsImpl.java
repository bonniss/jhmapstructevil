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
import xyz.jhmapstruct.domain.NextSupplierVi;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierViRepositoryWithBagRelationshipsImpl implements NextSupplierViRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERVIS_PARAMETER = "nextSupplierVis";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierVi> fetchBagRelationships(Optional<NextSupplierVi> nextSupplierVi) {
        return nextSupplierVi.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierVi> fetchBagRelationships(Page<NextSupplierVi> nextSupplierVis) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierVis.getContent()),
            nextSupplierVis.getPageable(),
            nextSupplierVis.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierVi> fetchBagRelationships(List<NextSupplierVi> nextSupplierVis) {
        return Optional.of(nextSupplierVis).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierVi fetchProducts(NextSupplierVi result) {
        return entityManager
            .createQuery(
                "select nextSupplierVi from NextSupplierVi nextSupplierVi left join fetch nextSupplierVi.products where nextSupplierVi.id = :id",
                NextSupplierVi.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierVi> fetchProducts(List<NextSupplierVi> nextSupplierVis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierVis.size()).forEach(index -> order.put(nextSupplierVis.get(index).getId(), index));
        List<NextSupplierVi> result = entityManager
            .createQuery(
                "select nextSupplierVi from NextSupplierVi nextSupplierVi left join fetch nextSupplierVi.products where nextSupplierVi in :nextSupplierVis",
                NextSupplierVi.class
            )
            .setParameter(NEXTSUPPLIERVIS_PARAMETER, nextSupplierVis)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
