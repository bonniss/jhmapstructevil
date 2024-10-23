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
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.repository.ProductThetaRepository;
import xyz.jhmapstruct.service.criteria.ProductThetaCriteria;

/**
 * Service for executing complex queries for {@link ProductTheta} entities in the database.
 * The main input is a {@link ProductThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductThetaQueryService extends QueryService<ProductTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductThetaQueryService.class);

    private final ProductThetaRepository productThetaRepository;

    public ProductThetaQueryService(ProductThetaRepository productThetaRepository) {
        this.productThetaRepository = productThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTheta> findByCriteria(ProductThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductTheta> specification = createSpecification(criteria);
        return productThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductTheta> specification = createSpecification(criteria);
        return productThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductTheta> createSpecification(ProductThetaCriteria criteria) {
        Specification<ProductTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductTheta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductTheta_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductTheta_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductTheta_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductTheta_.category, JoinType.LEFT).get(CategoryTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductTheta_.order, JoinType.LEFT).get(OrderTheta_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductTheta_.suppliers, JoinType.LEFT).get(SupplierTheta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
