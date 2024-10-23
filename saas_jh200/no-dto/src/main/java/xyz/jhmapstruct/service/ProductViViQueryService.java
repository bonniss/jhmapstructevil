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
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.criteria.ProductViViCriteria;

/**
 * Service for executing complex queries for {@link ProductViVi} entities in the database.
 * The main input is a {@link ProductViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductViViQueryService extends QueryService<ProductViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViQueryService.class);

    private final ProductViViRepository productViViRepository;

    public ProductViViQueryService(ProductViViRepository productViViRepository) {
        this.productViViRepository = productViViRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductViVi> findByCriteria(ProductViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductViVi> specification = createSpecification(criteria);
        return productViViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductViVi> specification = createSpecification(criteria);
        return productViViRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductViVi> createSpecification(ProductViViCriteria criteria) {
        Specification<ProductViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductViVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductViVi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductViVi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductViVi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductViVi_.category, JoinType.LEFT).get(CategoryViVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductViVi_.order, JoinType.LEFT).get(OrderViVi_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductViVi_.suppliers, JoinType.LEFT).get(SupplierViVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
