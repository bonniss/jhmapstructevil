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
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.repository.NextShipmentThetaRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentThetaCriteria;

/**
 * Service for executing complex queries for {@link NextShipmentTheta} entities in the database.
 * The main input is a {@link NextShipmentThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentThetaQueryService extends QueryService<NextShipmentTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentThetaQueryService.class);

    private final NextShipmentThetaRepository nextShipmentThetaRepository;

    public NextShipmentThetaQueryService(NextShipmentThetaRepository nextShipmentThetaRepository) {
        this.nextShipmentThetaRepository = nextShipmentThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentTheta> findByCriteria(NextShipmentThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentTheta> specification = createSpecification(criteria);
        return nextShipmentThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentTheta> specification = createSpecification(criteria);
        return nextShipmentThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentTheta> createSpecification(NextShipmentThetaCriteria criteria) {
        Specification<NextShipmentTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentTheta_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTrackingNumber(), NextShipmentTheta_.trackingNumber)
                );
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentTheta_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentTheta_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
