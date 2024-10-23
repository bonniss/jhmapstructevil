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
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.repository.ProductSigmaRepository;
import xyz.jhmapstruct.service.criteria.ProductSigmaCriteria;

/**
 * Service for executing complex queries for {@link ProductSigma} entities in the database.
 * The main input is a {@link ProductSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductSigmaQueryService extends QueryService<ProductSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductSigmaQueryService.class);

    private final ProductSigmaRepository productSigmaRepository;

    public ProductSigmaQueryService(ProductSigmaRepository productSigmaRepository) {
        this.productSigmaRepository = productSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductSigma> findByCriteria(ProductSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductSigma> specification = createSpecification(criteria);
        return productSigmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductSigma> specification = createSpecification(criteria);
        return productSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductSigma> createSpecification(ProductSigmaCriteria criteria) {
        Specification<ProductSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductSigma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductSigma_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductSigma_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductSigma_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductSigma_.category, JoinType.LEFT).get(CategorySigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductSigma_.order, JoinType.LEFT).get(OrderSigma_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductSigma_.suppliers, JoinType.LEFT).get(SupplierSigma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
