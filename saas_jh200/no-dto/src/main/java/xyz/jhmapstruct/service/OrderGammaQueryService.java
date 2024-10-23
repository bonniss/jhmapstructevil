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
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.repository.OrderGammaRepository;
import xyz.jhmapstruct.service.criteria.OrderGammaCriteria;

/**
 * Service for executing complex queries for {@link OrderGamma} entities in the database.
 * The main input is a {@link OrderGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderGammaQueryService extends QueryService<OrderGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderGammaQueryService.class);

    private final OrderGammaRepository orderGammaRepository;

    public OrderGammaQueryService(OrderGammaRepository orderGammaRepository) {
        this.orderGammaRepository = orderGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderGamma> findByCriteria(OrderGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderGamma> specification = createSpecification(criteria);
        return orderGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderGamma> specification = createSpecification(criteria);
        return orderGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderGamma> createSpecification(OrderGammaCriteria criteria) {
        Specification<OrderGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderGamma_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderGamma_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderGamma_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderGamma_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(OrderGamma_.products, JoinType.LEFT).get(ProductGamma_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderGamma_.payment, JoinType.LEFT).get(PaymentGamma_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(OrderGamma_.shipment, JoinType.LEFT).get(ShipmentGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(OrderGamma_.customer, JoinType.LEFT).get(CustomerGamma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
