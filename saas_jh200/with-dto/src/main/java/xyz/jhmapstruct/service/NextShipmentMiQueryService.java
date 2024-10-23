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
import xyz.jhmapstruct.domain.NextShipmentMi;
import xyz.jhmapstruct.repository.NextShipmentMiRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentMiCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentMiDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentMiMapper;

/**
 * Service for executing complex queries for {@link NextShipmentMi} entities in the database.
 * The main input is a {@link NextShipmentMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentMiQueryService extends QueryService<NextShipmentMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiQueryService.class);

    private final NextShipmentMiRepository nextShipmentMiRepository;

    private final NextShipmentMiMapper nextShipmentMiMapper;

    public NextShipmentMiQueryService(NextShipmentMiRepository nextShipmentMiRepository, NextShipmentMiMapper nextShipmentMiMapper) {
        this.nextShipmentMiRepository = nextShipmentMiRepository;
        this.nextShipmentMiMapper = nextShipmentMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentMiDTO> findByCriteria(NextShipmentMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentMi> specification = createSpecification(criteria);
        return nextShipmentMiRepository.findAll(specification, page).map(nextShipmentMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentMi> specification = createSpecification(criteria);
        return nextShipmentMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentMi> createSpecification(NextShipmentMiCriteria criteria) {
        Specification<NextShipmentMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentMi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), NextShipmentMi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentMi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentMi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
