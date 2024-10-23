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
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.repository.NextOrderThetaRepository;
import xyz.jhmapstruct.service.criteria.NextOrderThetaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderThetaDTO;
import xyz.jhmapstruct.service.mapper.NextOrderThetaMapper;

/**
 * Service for executing complex queries for {@link NextOrderTheta} entities in the database.
 * The main input is a {@link NextOrderThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextOrderThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextOrderThetaQueryService extends QueryService<NextOrderTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderThetaQueryService.class);

    private final NextOrderThetaRepository nextOrderThetaRepository;

    private final NextOrderThetaMapper nextOrderThetaMapper;

    public NextOrderThetaQueryService(NextOrderThetaRepository nextOrderThetaRepository, NextOrderThetaMapper nextOrderThetaMapper) {
        this.nextOrderThetaRepository = nextOrderThetaRepository;
        this.nextOrderThetaMapper = nextOrderThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextOrderThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOrderThetaDTO> findByCriteria(NextOrderThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextOrderTheta> specification = createSpecification(criteria);
        return nextOrderThetaRepository.findAll(specification, page).map(nextOrderThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextOrderThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextOrderTheta> specification = createSpecification(criteria);
        return nextOrderThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextOrderThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextOrderTheta> createSpecification(NextOrderThetaCriteria criteria) {
        Specification<NextOrderTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextOrderTheta_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), NextOrderTheta_.orderDate));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), NextOrderTheta_.totalPrice));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), NextOrderTheta_.status));
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextOrderTheta_.products, JoinType.LEFT).get(NextProductTheta_.id)
                    )
                );
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPaymentId(), root ->
                        root.join(NextOrderTheta_.payment, JoinType.LEFT).get(NextPaymentTheta_.id)
                    )
                );
            }
            if (criteria.getShipmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getShipmentId(), root ->
                        root.join(NextOrderTheta_.shipment, JoinType.LEFT).get(NextShipmentTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextOrderTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(NextOrderTheta_.customer, JoinType.LEFT).get(NextCustomerTheta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
