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
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.repository.NextShipmentAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentAlphaMapper;

/**
 * Service for executing complex queries for {@link NextShipmentAlpha} entities in the database.
 * The main input is a {@link NextShipmentAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentAlphaQueryService extends QueryService<NextShipmentAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentAlphaQueryService.class);

    private final NextShipmentAlphaRepository nextShipmentAlphaRepository;

    private final NextShipmentAlphaMapper nextShipmentAlphaMapper;

    public NextShipmentAlphaQueryService(
        NextShipmentAlphaRepository nextShipmentAlphaRepository,
        NextShipmentAlphaMapper nextShipmentAlphaMapper
    ) {
        this.nextShipmentAlphaRepository = nextShipmentAlphaRepository;
        this.nextShipmentAlphaMapper = nextShipmentAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentAlphaDTO> findByCriteria(NextShipmentAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentAlpha> specification = createSpecification(criteria);
        return nextShipmentAlphaRepository.findAll(specification, page).map(nextShipmentAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentAlpha> specification = createSpecification(criteria);
        return nextShipmentAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentAlpha> createSpecification(NextShipmentAlphaCriteria criteria) {
        Specification<NextShipmentAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentAlpha_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTrackingNumber(), NextShipmentAlpha_.trackingNumber)
                );
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentAlpha_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentAlpha_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
