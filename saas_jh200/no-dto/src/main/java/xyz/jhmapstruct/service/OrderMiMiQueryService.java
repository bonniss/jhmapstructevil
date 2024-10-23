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
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.repository.OrderMiMiRepository;
import xyz.jhmapstruct.service.criteria.OrderMiMiCriteria;

/**
 * Service for executing complex queries for {@link OrderMiMi} entities in the database.
 * The main input is a {@link OrderMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderMiMiQueryService extends QueryService<OrderMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiMiQueryService.class);

    private final OrderMiMiRepository orderMiMiRepository;

    public OrderMiMiQueryService(OrderMiMiRepository orderMiMiRepository) {
        this.orderMiMiRepository = orderMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderMiMi> findByCriteria(OrderMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderMiMi> specification = createSpecification(criteria);
        return orderMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderMiMi> specification = createSpecification(criteria);
        return orderMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderMiMi> createSpecification(OrderMiMiCriteria criteria) {
        Specification<OrderMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderMiMi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderMiMi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderMiMi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderMiMi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(OrderMiMi_.products, JoinType.LEFT).get(ProductMiMi_.id))
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderMiMi_.payment, JoinType.LEFT).get(PaymentMiMi_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(OrderMiMi_.shipment, JoinType.LEFT).get(ShipmentMiMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(OrderMiMi_.customer, JoinType.LEFT).get(CustomerMiMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
