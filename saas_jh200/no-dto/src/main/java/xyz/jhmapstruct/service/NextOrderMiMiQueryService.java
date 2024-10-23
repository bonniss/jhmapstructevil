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
import xyz.jhmapstruct.domain.NextOrderMiMi;
import xyz.jhmapstruct.repository.NextOrderMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextOrderMiMiCriteria;

/**
 * Service for executing complex queries for {@link NextOrderMiMi} entities in the database.
 * The main input is a {@link NextOrderMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderMiMiQueryService extends QueryService<NextOrderMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiMiQueryService.class);

    private final NextOrderMiMiRepository nextOrderMiMiRepository;

    public NextOrderMiMiQueryService(NextOrderMiMiRepository nextOrderMiMiRepository) {
        this.nextOrderMiMiRepository = nextOrderMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextOrderMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderMiMi> findByCriteria(NextOrderMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderMiMi> specification = createSpecification(criteria);
        return nextOrderMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderMiMi> specification = createSpecification(criteria);
        return nextOrderMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderMiMi> createSpecification(NextOrderMiMiCriteria criteria) {
        Specification<NextOrderMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderMiMi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderMiMi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderMiMi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderMiMi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderMiMi_.products, JoinType.LEFT).get(NextProductMiMi_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderMiMi_.payment, JoinType.LEFT).get(NextPaymentMiMi_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderMiMi_.shipment, JoinType.LEFT).get(NextShipmentMiMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextOrderMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderMiMi_.customer, JoinType.LEFT).get(NextCustomerMiMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
