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
import xyz.jhmapstruct.domain.OrderSigma;
import xyz.jhmapstruct.repository.OrderSigmaRepository;
import xyz.jhmapstruct.service.criteria.OrderSigmaCriteria;

/**
 * Service for executing complex queries for {@link OrderSigma} entities in the database.
 * The main input is a {@link OrderSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderSigmaQueryService extends QueryService<OrderSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderSigmaQueryService.class);

    private final OrderSigmaRepository orderSigmaRepository;

    public OrderSigmaQueryService(OrderSigmaRepository orderSigmaRepository) {
        this.orderSigmaRepository = orderSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderSigma> findByCriteria(OrderSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderSigma> specification = createSpecification(criteria);
        return orderSigmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderSigma> specification = createSpecification(criteria);
        return orderSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderSigma> createSpecification(OrderSigmaCriteria criteria) {
        Specification<OrderSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderSigma_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderSigma_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderSigma_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderSigma_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(OrderSigma_.products, JoinType.LEFT).get(ProductSigma_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderSigma_.payment, JoinType.LEFT).get(PaymentSigma_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(OrderSigma_.shipment, JoinType.LEFT).get(ShipmentSigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(OrderSigma_.customer, JoinType.LEFT).get(CustomerSigma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
