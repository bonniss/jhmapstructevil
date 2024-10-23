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
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.repository.NextOrderRepository;
import xyz.jhmapstruct.service.criteria.NextOrderCriteria;
import xyz.jhmapstruct.service.dto.NextOrderDTO;
import xyz.jhmapstruct.service.mapper.NextOrderMapper;

/**
 * Service for executing complex queries for {@link NextOrder} entities in the database.
 * The main input is a {@link NextOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderQueryService extends QueryService<NextOrder> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderQueryService.class);

    private final NextOrderRepository nextOrderRepository;

    private final NextOrderMapper nextOrderMapper;

    public NextOrderQueryService(NextOrderRepository nextOrderRepository, NextOrderMapper nextOrderMapper) {
        this.nextOrderRepository = nextOrderRepository;
        this.nextOrderMapper = nextOrderMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderDTO> findByCriteria(NextOrderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrder> specification = createSpecification(criteria);
        return nextOrderRepository.findAll(specification, page).map(nextOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrder> specification = createSpecification(criteria);
        return nextOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrder> createSpecification(NextOrderCriteria criteria) {
        Specification<NextOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrder_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrder_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrder_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrder_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(NextOrder_.products, JoinType.LEFT).get(NextProduct_.id))
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(NextOrder_.payment, JoinType.LEFT).get(NextPayment_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(NextOrder_.shipment, JoinType.LEFT).get(NextShipment_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextOrder_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(NextOrder_.customer, JoinType.LEFT).get(NextCustomer_.id)
                    )
                );
            }
        }
        return specification;
    }
}
