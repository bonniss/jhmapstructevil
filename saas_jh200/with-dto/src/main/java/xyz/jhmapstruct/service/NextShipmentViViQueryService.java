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
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.repository.NextShipmentViViRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentViViCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentViViDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentViViMapper;

/**
 * Service for executing complex queries for {@link NextShipmentViVi} entities in the database.
 * The main input is a {@link NextShipmentViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentViViQueryService extends QueryService<NextShipmentViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViViQueryService.class);

    private final NextShipmentViViRepository nextShipmentViViRepository;

    private final NextShipmentViViMapper nextShipmentViViMapper;

    public NextShipmentViViQueryService(
        NextShipmentViViRepository nextShipmentViViRepository,
        NextShipmentViViMapper nextShipmentViViMapper
    ) {
        this.nextShipmentViViRepository = nextShipmentViViRepository;
        this.nextShipmentViViMapper = nextShipmentViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentViViDTO> findByCriteria(NextShipmentViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentViVi> specification = createSpecification(criteria);
        return nextShipmentViViRepository.findAll(specification, page).map(nextShipmentViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentViVi> specification = createSpecification(criteria);
        return nextShipmentViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentViVi> createSpecification(NextShipmentViViCriteria criteria) {
        Specification<NextShipmentViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentViVi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), NextShipmentViVi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentViVi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentViVi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
