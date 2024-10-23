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
import xyz.jhmapstruct.domain.NextOrderMi;
import xyz.jhmapstruct.repository.NextOrderMiRepository;
import xyz.jhmapstruct.service.criteria.NextOrderMiCriteria;
import xyz.jhmapstruct.service.dto.NextOrderMiDTO;
import xyz.jhmapstruct.service.mapper.NextOrderMiMapper;

/**
 * Service for executing complex queries for {@link NextOrderMi} entities in the database.
 * The main input is a {@link NextOrderMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderMiQueryService extends QueryService<NextOrderMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiQueryService.class);

    private final NextOrderMiRepository nextOrderMiRepository;

    private final NextOrderMiMapper nextOrderMiMapper;

    public NextOrderMiQueryService(NextOrderMiRepository nextOrderMiRepository, NextOrderMiMapper nextOrderMiMapper) {
        this.nextOrderMiRepository = nextOrderMiRepository;
        this.nextOrderMiMapper = nextOrderMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderMiDTO> findByCriteria(NextOrderMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderMi> specification = createSpecification(criteria);
        return nextOrderMiRepository.findAll(specification, page).map(nextOrderMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderMi> specification = createSpecification(criteria);
        return nextOrderMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderMi> createSpecification(NextOrderMiCriteria criteria) {
        Specification<NextOrderMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderMi_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderMi_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderMi_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderMi_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderMi_.products, JoinType.LEFT).get(NextProductMi_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root -> root.join(NextOrderMi_.payment, JoinType.LEFT).get(PaymentMi_.id))
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root -> root.join(NextOrderMi_.shipment, JoinType.LEFT).get(ShipmentMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextOrderMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(NextOrderMi_.customer, JoinType.LEFT).get(CustomerMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
