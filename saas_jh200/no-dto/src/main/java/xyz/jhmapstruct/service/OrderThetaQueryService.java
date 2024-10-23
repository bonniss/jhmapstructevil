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
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.repository.OrderThetaRepository;
import xyz.jhmapstruct.service.criteria.OrderThetaCriteria;

/**
 * Service for executing complex queries for {@link OrderTheta} entities in the database.
 * The main input is a {@link OrderThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderThetaQueryService extends QueryService<OrderTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderThetaQueryService.class);

    private final OrderThetaRepository orderThetaRepository;

    public OrderThetaQueryService(OrderThetaRepository orderThetaRepository) {
        this.orderThetaRepository = orderThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderTheta> findByCriteria(OrderThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderTheta> specification = createSpecification(criteria);
        return orderThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderTheta> specification = createSpecification(criteria);
        return orderThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderTheta> createSpecification(OrderThetaCriteria criteria) {
        Specification<OrderTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderTheta_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderTheta_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderTheta_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderTheta_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(OrderTheta_.products, JoinType.LEFT).get(ProductTheta_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderTheta_.payment, JoinType.LEFT).get(PaymentTheta_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(OrderTheta_.shipment, JoinType.LEFT).get(ShipmentTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(OrderTheta_.customer, JoinType.LEFT).get(CustomerTheta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
