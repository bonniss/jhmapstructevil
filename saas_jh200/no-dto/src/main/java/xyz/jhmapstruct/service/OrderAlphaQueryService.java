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
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.repository.OrderAlphaRepository;
import xyz.jhmapstruct.service.criteria.OrderAlphaCriteria;

/**
 * Service for executing complex queries for {@link OrderAlpha} entities in the database.
 * The main input is a {@link OrderAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderAlpha} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderAlphaQueryService extends QueryService<OrderAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAlphaQueryService.class);

    private final OrderAlphaRepository orderAlphaRepository;

    public OrderAlphaQueryService(OrderAlphaRepository orderAlphaRepository) {
        this.orderAlphaRepository = orderAlphaRepository;
    }

    /**
     * Return a {@link Page} of {@link OrderAlpha} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderAlpha> findByCriteria(OrderAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderAlpha> specification = createSpecification(criteria);
        return orderAlphaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderAlpha> specification = createSpecification(criteria);
        return orderAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderAlpha> createSpecification(OrderAlphaCriteria criteria) {
        Specification<OrderAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderAlpha_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderAlpha_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderAlpha_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderAlpha_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(OrderAlpha_.products, JoinType.LEFT).get(ProductAlpha_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderAlpha_.payment, JoinType.LEFT).get(PaymentAlpha_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(OrderAlpha_.shipment, JoinType.LEFT).get(ShipmentAlpha_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(OrderAlpha_.customer, JoinType.LEFT).get(CustomerAlpha_.id)
                    )
                );
            }
        }
        return specification;
    }
}
