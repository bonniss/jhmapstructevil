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
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.repository.NextShipmentViRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentViCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentViDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentViMapper;

/**
 * Service for executing complex queries for {@link NextShipmentVi} entities in the database.
 * The main input is a {@link NextShipmentViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentViQueryService extends QueryService<NextShipmentVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViQueryService.class);

    private final NextShipmentViRepository nextShipmentViRepository;

    private final NextShipmentViMapper nextShipmentViMapper;

    public NextShipmentViQueryService(NextShipmentViRepository nextShipmentViRepository, NextShipmentViMapper nextShipmentViMapper) {
        this.nextShipmentViRepository = nextShipmentViRepository;
        this.nextShipmentViMapper = nextShipmentViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentViDTO> findByCriteria(NextShipmentViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentVi> specification = createSpecification(criteria);
        return nextShipmentViRepository.findAll(specification, page).map(nextShipmentViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentVi> specification = createSpecification(criteria);
        return nextShipmentViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentVi> createSpecification(NextShipmentViCriteria criteria) {
        Specification<NextShipmentVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentVi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), NextShipmentVi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentVi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentVi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
