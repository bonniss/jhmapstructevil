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
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.criteria.OrderViViCriteria;

/**
 * Service for executing complex queries for {@link OrderViVi} entities in the database.
 * The main input is a {@link OrderViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderViViQueryService extends QueryService<OrderViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViViQueryService.class);

    private final OrderViViRepository orderViViRepository;

    public OrderViViQueryService(OrderViViRepository orderViViRepository) {
        this.orderViViRepository = orderViViRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderViVi> findByCriteria(OrderViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderViVi> specification = createSpecification(criteria);
        return orderViViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderViVi> specification = createSpecification(criteria);
        return orderViViRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderViVi> createSpecification(OrderViViCriteria criteria) {
        Specification<OrderViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderViVi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderViVi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderViVi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderViVi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(OrderViVi_.products, JoinType.LEFT).get(ProductViVi_.id))
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderViVi_.payment, JoinType.LEFT).get(PaymentViVi_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(OrderViVi_.shipment, JoinType.LEFT).get(ShipmentViVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(OrderViVi_.customer, JoinType.LEFT).get(CustomerViVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
