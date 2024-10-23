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
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.repository.NextShipmentSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentSigmaMapper;

/**
 * Service for executing complex queries for {@link NextShipmentSigma} entities in the database.
 * The main input is a {@link NextShipmentSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentSigmaQueryService extends QueryService<NextShipmentSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentSigmaQueryService.class);

    private final NextShipmentSigmaRepository nextShipmentSigmaRepository;

    private final NextShipmentSigmaMapper nextShipmentSigmaMapper;

    public NextShipmentSigmaQueryService(
        NextShipmentSigmaRepository nextShipmentSigmaRepository,
        NextShipmentSigmaMapper nextShipmentSigmaMapper
    ) {
        this.nextShipmentSigmaRepository = nextShipmentSigmaRepository;
        this.nextShipmentSigmaMapper = nextShipmentSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentSigmaDTO> findByCriteria(NextShipmentSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentSigma> specification = createSpecification(criteria);
        return nextShipmentSigmaRepository.findAll(specification, page).map(nextShipmentSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentSigma> specification = createSpecification(criteria);
        return nextShipmentSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentSigma> createSpecification(NextShipmentSigmaCriteria criteria) {
        Specification<NextShipmentSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentSigma_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTrackingNumber(), NextShipmentSigma_.trackingNumber)
                );
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentSigma_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentSigma_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
