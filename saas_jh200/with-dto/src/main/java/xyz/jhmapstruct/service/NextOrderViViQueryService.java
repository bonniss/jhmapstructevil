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
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.repository.NextOrderViViRepository;
import xyz.jhmapstruct.service.criteria.NextOrderViViCriteria;
import xyz.jhmapstruct.service.dto.NextOrderViViDTO;
import xyz.jhmapstruct.service.mapper.NextOrderViViMapper;

/**
 * Service for executing complex queries for {@link NextOrderViVi} entities in the database.
 * The main input is a {@link NextOrderViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderViViQueryService extends QueryService<NextOrderViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViViQueryService.class);

    private final NextOrderViViRepository nextOrderViViRepository;

    private final NextOrderViViMapper nextOrderViViMapper;

    public NextOrderViViQueryService(NextOrderViViRepository nextOrderViViRepository, NextOrderViViMapper nextOrderViViMapper) {
        this.nextOrderViViRepository = nextOrderViViRepository;
        this.nextOrderViViMapper = nextOrderViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderViViDTO> findByCriteria(NextOrderViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderViVi> specification = createSpecification(criteria);
        return nextOrderViViRepository.findAll(specification, page).map(nextOrderViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderViVi> specification = createSpecification(criteria);
        return nextOrderViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderViVi> createSpecification(NextOrderViViCriteria criteria) {
        Specification<NextOrderViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderViVi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderViVi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderViVi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderViVi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderViVi_.products, JoinType.LEFT).get(NextProductViVi_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderViVi_.payment, JoinType.LEFT).get(NextPaymentViVi_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderViVi_.shipment, JoinType.LEFT).get(NextShipmentViVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextOrderViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderViVi_.customer, JoinType.LEFT).get(NextCustomerViVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
