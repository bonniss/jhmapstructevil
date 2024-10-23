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
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.repository.NextShipmentBetaRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentBetaCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentBetaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentBetaMapper;

/**
 * Service for executing complex queries for {@link NextShipmentBeta} entities in the database.
 * The main input is a {@link NextShipmentBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentBetaQueryService extends QueryService<NextShipmentBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentBetaQueryService.class);

    private final NextShipmentBetaRepository nextShipmentBetaRepository;

    private final NextShipmentBetaMapper nextShipmentBetaMapper;

    public NextShipmentBetaQueryService(
        NextShipmentBetaRepository nextShipmentBetaRepository,
        NextShipmentBetaMapper nextShipmentBetaMapper
    ) {
        this.nextShipmentBetaRepository = nextShipmentBetaRepository;
        this.nextShipmentBetaMapper = nextShipmentBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentBetaDTO> findByCriteria(NextShipmentBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentBeta> specification = createSpecification(criteria);
        return nextShipmentBetaRepository.findAll(specification, page).map(nextShipmentBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentBeta> specification = createSpecification(criteria);
        return nextShipmentBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentBeta> createSpecification(NextShipmentBetaCriteria criteria) {
        Specification<NextShipmentBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentBeta_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), NextShipmentBeta_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentBeta_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentBeta_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
