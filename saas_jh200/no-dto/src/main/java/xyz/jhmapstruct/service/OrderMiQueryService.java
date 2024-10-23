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
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.criteria.OrderMiCriteria;

/**
 * Service for executing complex queries for {@link OrderMi} entities in the database.
 * The main input is a {@link OrderMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderMiQueryService extends QueryService<OrderMi> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiQueryService.class);

    private final OrderMiRepository orderMiRepository;

    public OrderMiQueryService(OrderMiRepository orderMiRepository) {
        this.orderMiRepository = orderMiRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderMi> findByCriteria(OrderMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderMi> specification = createSpecification(criteria);
        return orderMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderMi> specification = createSpecification(criteria);
        return orderMiRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderMi> createSpecification(OrderMiCriteria criteria) {
        Specification<OrderMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderMi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderMi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderMi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderMi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(OrderMi_.products, JoinType.LEFT).get(ProductMi_.id))
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderMi_.payment, JoinType.LEFT).get(NextPaymentMi_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(OrderMi_.shipment, JoinType.LEFT).get(NextShipmentMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(OrderMi_.customer, JoinType.LEFT).get(NextCustomerMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
