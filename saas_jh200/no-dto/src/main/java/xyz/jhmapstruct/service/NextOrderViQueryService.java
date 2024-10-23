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
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.repository.NextOrderViRepository;
import xyz.jhmapstruct.service.criteria.NextOrderViCriteria;

/**
 * Service for executing complex queries for {@link NextOrderVi} entities in the database.
 * The main input is a {@link NextOrderViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderViQueryService extends QueryService<NextOrderVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViQueryService.class);

    private final NextOrderViRepository nextOrderViRepository;

    public NextOrderViQueryService(NextOrderViRepository nextOrderViRepository) {
        this.nextOrderViRepository = nextOrderViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextOrderVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderVi> findByCriteria(NextOrderViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderVi> specification = createSpecification(criteria);
        return nextOrderViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderVi> specification = createSpecification(criteria);
        return nextOrderViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderVi> createSpecification(NextOrderViCriteria criteria) {
        Specification<NextOrderVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderVi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderVi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderVi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderVi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderVi_.products, JoinType.LEFT).get(NextProductVi_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderVi_.payment, JoinType.LEFT).get(NextPaymentVi_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderVi_.shipment, JoinType.LEFT).get(NextShipmentVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextOrderVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderVi_.customer, JoinType.LEFT).get(NextCustomerVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
