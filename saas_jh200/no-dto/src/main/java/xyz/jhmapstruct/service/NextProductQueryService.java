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
import xyz.jhmapstruct.domain.NextProduct;
import xyz.jhmapstruct.repository.NextProductRepository;
import xyz.jhmapstruct.service.criteria.NextProductCriteria;

/**
 * Service for executing complex queries for {@link NextProduct} entities in the database.
 * The main input is a {@link NextProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductQueryService extends QueryService<NextProduct> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductQueryService.class);

    private final NextProductRepository nextProductRepository;

    public NextProductQueryService(NextProductRepository nextProductRepository) {
        this.nextProductRepository = nextProductRepository;
    }

    /**
     * Return a {@link Page} of {@link NextProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProduct> findByCriteria(NextProductCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProduct> specification = createSpecification(criteria);
        return nextProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProduct> specification = createSpecification(criteria);
        return nextProductRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProduct> createSpecification(NextProductCriteria criteria) {
        Specification<NextProduct> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProduct_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProduct_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProduct_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProduct_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProduct_.category, JoinType.LEFT).get(NextCategory_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextProduct_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(NextProduct_.order, JoinType.LEFT).get(NextOrder_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProduct_.suppliers, JoinType.LEFT).get(NextSupplier_.id)
                    )
                );
            }
        }
        return specification;
    }
}
