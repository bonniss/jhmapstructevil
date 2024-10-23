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
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.repository.OrderViRepository;
import xyz.jhmapstruct.service.criteria.OrderViCriteria;
import xyz.jhmapstruct.service.dto.OrderViDTO;
import xyz.jhmapstruct.service.mapper.OrderViMapper;

/**
 * Service for executing complex queries for {@link OrderVi} entities in the database.
 * The main input is a {@link OrderViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OrderViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderViQueryService extends QueryService<OrderVi> {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViQueryService.class);

    private final OrderViRepository orderViRepository;

    private final OrderViMapper orderViMapper;

    public OrderViQueryService(OrderViRepository orderViRepository, OrderViMapper orderViMapper) {
        this.orderViRepository = orderViRepository;
        this.orderViMapper = orderViMapper;
    }

    /**
     * Return a {@link Page} of {@link OrderViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderViDTO> findByCriteria(OrderViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderVi> specification = createSpecification(criteria);
        return orderViRepository.findAll(specification, page).map(orderViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrderVi> specification = createSpecification(criteria);
        return orderViRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderVi> createSpecification(OrderViCriteria criteria) {
        Specification<OrderVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderVi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), OrderVi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderVi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), OrderVi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(OrderVi_.products, JoinType.LEFT).get(ProductVi_.id))
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(OrderVi_.payment, JoinType.LEFT).get(PaymentVi_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(OrderVi_.shipment, JoinType.LEFT).get(ShipmentVi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(OrderVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(OrderVi_.customer, JoinType.LEFT).get(CustomerVi_.id))
                );
            }
        }
        return specification;
    }
}
