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
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.repository.NextOrderGammaRepository;
import xyz.jhmapstruct.service.criteria.NextOrderGammaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderGammaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderGammaMapper;

/**
 * Service for executing complex queries for {@link NextOrderGamma} entities in the database.
 * The main input is a {@link NextOrderGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderGammaQueryService extends QueryService<NextOrderGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderGammaQueryService.class);

    private final NextOrderGammaRepository nextOrderGammaRepository;

    private final NextOrderGammaMapper nextOrderGammaMapper;

    public NextOrderGammaQueryService(NextOrderGammaRepository nextOrderGammaRepository, NextOrderGammaMapper nextOrderGammaMapper) {
        this.nextOrderGammaRepository = nextOrderGammaRepository;
        this.nextOrderGammaMapper = nextOrderGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderGammaDTO> findByCriteria(NextOrderGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderGamma> specification = createSpecification(criteria);
        return nextOrderGammaRepository.findAll(specification, page).map(nextOrderGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderGamma> specification = createSpecification(criteria);
        return nextOrderGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderGamma> createSpecification(NextOrderGammaCriteria criteria) {
        Specification<NextOrderGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderGamma_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderGamma_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderGamma_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderGamma_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderGamma_.products, JoinType.LEFT).get(NextProductGamma_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderGamma_.payment, JoinType.LEFT).get(NextPaymentGamma_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderGamma_.shipment, JoinType.LEFT).get(NextShipmentGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextOrderGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderGamma_.customer, JoinType.LEFT).get(NextCustomerGamma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
