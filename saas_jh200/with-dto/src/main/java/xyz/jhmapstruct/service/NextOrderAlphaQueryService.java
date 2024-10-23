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
import xyz.jhmapstruct.domain.NextOrderAlpha;
import xyz.jhmapstruct.repository.NextOrderAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextOrderAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderAlphaMapper;

/**
 * Service for executing complex queries for {@link NextOrderAlpha} entities in the database.
 * The main input is a {@link NextOrderAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderAlphaQueryService extends QueryService<NextOrderAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderAlphaQueryService.class);

    private final NextOrderAlphaRepository nextOrderAlphaRepository;

    private final NextOrderAlphaMapper nextOrderAlphaMapper;

    public NextOrderAlphaQueryService(NextOrderAlphaRepository nextOrderAlphaRepository, NextOrderAlphaMapper nextOrderAlphaMapper) {
        this.nextOrderAlphaRepository = nextOrderAlphaRepository;
        this.nextOrderAlphaMapper = nextOrderAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderAlphaDTO> findByCriteria(NextOrderAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderAlpha> specification = createSpecification(criteria);
        return nextOrderAlphaRepository.findAll(specification, page).map(nextOrderAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderAlpha> specification = createSpecification(criteria);
        return nextOrderAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderAlpha> createSpecification(NextOrderAlphaCriteria criteria) {
        Specification<NextOrderAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderAlpha_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderAlpha_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderAlpha_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderAlpha_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderAlpha_.products, JoinType.LEFT).get(NextProductAlpha_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderAlpha_.payment, JoinType.LEFT).get(NextPaymentAlpha_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderAlpha_.shipment, JoinType.LEFT).get(NextShipmentAlpha_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextOrderAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderAlpha_.customer, JoinType.LEFT).get(NextCustomerAlpha_.id)
                    )
                );
            }
        }
        return specification;
    }
}
