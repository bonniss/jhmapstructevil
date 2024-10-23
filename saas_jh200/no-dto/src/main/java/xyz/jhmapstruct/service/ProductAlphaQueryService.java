package xyz.jhmapstruct.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import xyz.jhmapstruct.domain.*; // for static metamodels
import xyz.jhmapstruct.domain.ProductAlpha;
import xyz.jhmapstruct.repository.ProductAlphaRepository;
import xyz.jhmapstruct.service.criteria.ProductAlphaCriteria;

/**
 * Service for executing complex queries for {@link ProductAlpha} entities in the database.
 * The main input is a {@link ProductAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductAlpha} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductAlphaQueryService extends QueryService<ProductAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAlphaQueryService.class);

    private final ProductAlphaRepository productAlphaRepository;

    public ProductAlphaQueryService(ProductAlphaRepository productAlphaRepository) {
        this.productAlphaRepository = productAlphaRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductAlpha} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductAlpha> findByCriteria(ProductAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductAlpha> specification = createSpecification(criteria);
        return productAlphaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductAlpha> specification = createSpecification(criteria);
        return productAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductAlpha> createSpecification(ProductAlphaCriteria criteria) {
        Specification<ProductAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductAlpha_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductAlpha_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductAlpha_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductAlpha_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductAlpha_.category, JoinType.LEFT).get(CategoryAlpha_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductAlpha_.order, JoinType.LEFT).get(OrderAlpha_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductAlpha_.suppliers, JoinType.LEFT).get(SupplierAlpha_.id)
                    )
                );
            }
        }
        return specification;
    }
}
