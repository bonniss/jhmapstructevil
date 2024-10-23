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
import xyz.jhmapstruct.domain.NextShipment;
import xyz.jhmapstruct.repository.NextShipmentRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentMapper;

/**
 * Service for executing complex queries for {@link NextShipment} entities in the database.
 * The main input is a {@link NextShipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentQueryService extends QueryService<NextShipment> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentQueryService.class);

    private final NextShipmentRepository nextShipmentRepository;

    private final NextShipmentMapper nextShipmentMapper;

    public NextShipmentQueryService(NextShipmentRepository nextShipmentRepository, NextShipmentMapper nextShipmentMapper) {
        this.nextShipmentRepository = nextShipmentRepository;
        this.nextShipmentMapper = nextShipmentMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentDTO> findByCriteria(NextShipmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipment> specification = createSpecification(criteria);
        return nextShipmentRepository.findAll(specification, page).map(nextShipmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipment> specification = createSpecification(criteria);
        return nextShipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipment> createSpecification(NextShipmentCriteria criteria) {
        Specification<NextShipment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipment_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), NextShipment_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipment_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipment_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextShipment_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
