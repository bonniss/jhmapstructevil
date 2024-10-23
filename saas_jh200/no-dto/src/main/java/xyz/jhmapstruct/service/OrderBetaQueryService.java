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
import xyz.jhmapstruct.domain.OrderBeta;
import xyz.jhmapstruct.repository.OrderBetaRepository;
import xyz.jhmapstruct.service.criteria.OrderBetaCriteria;

/**
 * Service for executing complex queries for {@link OrderBeta} entities in the database.
 * The main input is a {@link OrderBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderBetaQueryService extends QueryService<OrderBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBetaQueryService.class);

    private final OrderBetaRepository orderBetaRepository;

    public OrderBetaQueryService(OrderBetaRepository orderBetaRepository) {
        this.orderBetaRepository = orderBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderBeta> findByCriteria(OrderBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderBeta> specification = createSpecification(criteria);
        return orderBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderBeta> specification = createSpecification(criteria);
        return orderBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderBeta> createSpecification(OrderBetaCriteria criteria) {
        Specification<OrderBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderBeta_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderBeta_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderBeta_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderBeta_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(OrderBeta_.products, JoinType.LEFT).get(ProductBeta_.id))
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderBeta_.payment, JoinType.LEFT).get(PaymentBeta_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(OrderBeta_.shipment, JoinType.LEFT).get(ShipmentBeta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(OrderBeta_.customer, JoinType.LEFT).get(CustomerBeta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
