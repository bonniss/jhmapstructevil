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
import xyz.jhmapstruct.domain.NextShipmentMiMi;
import xyz.jhmapstruct.repository.NextShipmentMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentMiMiCriteria;

/**
 * Service for executing complex queries for {@link NextShipmentMiMi} entities in the database.
 * The main input is a {@link NextShipmentMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentMiMiQueryService extends QueryService<NextShipmentMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiMiQueryService.class);

    private final NextShipmentMiMiRepository nextShipmentMiMiRepository;

    public NextShipmentMiMiQueryService(NextShipmentMiMiRepository nextShipmentMiMiRepository) {
        this.nextShipmentMiMiRepository = nextShipmentMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentMiMi> findByCriteria(NextShipmentMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentMiMi> specification = createSpecification(criteria);
        return nextShipmentMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentMiMi> specification = createSpecification(criteria);
        return nextShipmentMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentMiMi> createSpecification(NextShipmentMiMiCriteria criteria) {
        Specification<NextShipmentMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentMiMi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), NextShipmentMiMi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentMiMi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentMiMi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
