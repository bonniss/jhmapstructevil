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
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.repository.NextOrderBetaRepository;
import xyz.jhmapstruct.service.criteria.NextOrderBetaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderBetaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderBetaMapper;

/**
 * Service for executing complex queries for {@link NextOrderBeta} entities in the database.
 * The main input is a {@link NextOrderBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderBetaQueryService extends QueryService<NextOrderBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderBetaQueryService.class);

    private final NextOrderBetaRepository nextOrderBetaRepository;

    private final NextOrderBetaMapper nextOrderBetaMapper;

    public NextOrderBetaQueryService(NextOrderBetaRepository nextOrderBetaRepository, NextOrderBetaMapper nextOrderBetaMapper) {
        this.nextOrderBetaRepository = nextOrderBetaRepository;
        this.nextOrderBetaMapper = nextOrderBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderBetaDTO> findByCriteria(NextOrderBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderBeta> specification = createSpecification(criteria);
        return nextOrderBetaRepository.findAll(specification, page).map(nextOrderBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderBeta> specification = createSpecification(criteria);
        return nextOrderBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderBeta> createSpecification(NextOrderBetaCriteria criteria) {
        Specification<NextOrderBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderBeta_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderBeta_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderBeta_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderBeta_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderBeta_.products, JoinType.LEFT).get(NextProductBeta_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderBeta_.payment, JoinType.LEFT).get(NextPaymentBeta_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderBeta_.shipment, JoinType.LEFT).get(NextShipmentBeta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextOrderBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderBeta_.customer, JoinType.LEFT).get(NextCustomerBeta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
