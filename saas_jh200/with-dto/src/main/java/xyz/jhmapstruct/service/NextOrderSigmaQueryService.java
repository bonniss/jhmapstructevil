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
import xyz.jhmapstruct.domain.NextOrderSigma;
import xyz.jhmapstruct.repository.NextOrderSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextOrderSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderSigmaMapper;

/**
 * Service for executing complex queries for {@link NextOrderSigma} entities in the database.
 * The main input is a {@link NextOrderSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderSigmaQueryService extends QueryService<NextOrderSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderSigmaQueryService.class);

    private final NextOrderSigmaRepository nextOrderSigmaRepository;

    private final NextOrderSigmaMapper nextOrderSigmaMapper;

    public NextOrderSigmaQueryService(NextOrderSigmaRepository nextOrderSigmaRepository, NextOrderSigmaMapper nextOrderSigmaMapper) {
        this.nextOrderSigmaRepository = nextOrderSigmaRepository;
        this.nextOrderSigmaMapper = nextOrderSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderSigmaDTO> findByCriteria(NextOrderSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderSigma> specification = createSpecification(criteria);
        return nextOrderSigmaRepository.findAll(specification, page).map(nextOrderSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderSigma> specification = createSpecification(criteria);
        return nextOrderSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderSigma> createSpecification(NextOrderSigmaCriteria criteria) {
        Specification<NextOrderSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderSigma_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderSigma_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderSigma_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderSigma_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderSigma_.products, JoinType.LEFT).get(NextProductSigma_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderSigma_.payment, JoinType.LEFT).get(NextPaymentSigma_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderSigma_.shipment, JoinType.LEFT).get(NextShipmentSigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextOrderSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderSigma_.customer, JoinType.LEFT).get(NextCustomerSigma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
