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
import xyz.jhmapstruct.domain.NextSupplierAlpha;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NextSupplierAlphaRepositoryWithBagRelationshipsImpl implements NextSupplierAlphaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String NEXTSUPPLIERALPHAS_PARAMETER = "nextSupplierAlphas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<NextSupplierAlpha> fetchBagRelationships(Optional<NextSupplierAlpha> nextSupplierAlpha) {
        return nextSupplierAlpha.map(this::fetchProducts);
    }

    @Override
    public Page<NextSupplierAlpha> fetchBagRelationships(Page<NextSupplierAlpha> nextSupplierAlphas) {
        return new PageImpl<>(
            fetchBagRelationships(nextSupplierAlphas.getContent()),
            nextSupplierAlphas.getPageable(),
            nextSupplierAlphas.getTotalElements()
        );
    }

    @Override
    public List<NextSupplierAlpha> fetchBagRelationships(List<NextSupplierAlpha> nextSupplierAlphas) {
        return Optional.of(nextSupplierAlphas).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    NextSupplierAlpha fetchProducts(NextSupplierAlpha result) {
        return entityManager
            .createQuery(
                "select nextSupplierAlpha from NextSupplierAlpha nextSupplierAlpha left join fetch nextSupplierAlpha.products where nextSupplierAlpha.id = :id",
                NextSupplierAlpha.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<NextSupplierAlpha> fetchProducts(List<NextSupplierAlpha> nextSupplierAlphas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, nextSupplierAlphas.size()).forEach(index -> order.put(nextSupplierAlphas.get(index).getId(), index));
        List<NextSupplierAlpha> result = entityManager
            .createQuery(
                "select nextSupplierAlpha from NextSupplierAlpha nextSupplierAlpha left join fetch nextSupplierAlpha.products where nextSupplierAlpha in :nextSupplierAlphas",
                NextSupplierAlpha.class
            )
            .setParameter(NEXTSUPPLIERALPHAS_PARAMETER, nextSupplierAlphas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
