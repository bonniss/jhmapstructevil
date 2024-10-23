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
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.repository.ProductGammaRepository;
import xyz.jhmapstruct.service.criteria.ProductGammaCriteria;

/**
 * Service for executing complex queries for {@link ProductGamma} entities in the database.
 * The main input is a {@link ProductGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductGammaQueryService extends QueryService<ProductGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductGammaQueryService.class);

    private final ProductGammaRepository productGammaRepository;

    public ProductGammaQueryService(ProductGammaRepository productGammaRepository) {
        this.productGammaRepository = productGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductGamma> findByCriteria(ProductGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductGamma> specification = createSpecification(criteria);
        return productGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductGamma> specification = createSpecification(criteria);
        return productGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductGamma> createSpecification(ProductGammaCriteria criteria) {
        Specification<ProductGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductGamma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductGamma_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductGamma_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductGamma_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductGamma_.category, JoinType.LEFT).get(CategoryGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductGamma_.order, JoinType.LEFT).get(OrderGamma_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductGamma_.suppliers, JoinType.LEFT).get(SupplierGamma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
