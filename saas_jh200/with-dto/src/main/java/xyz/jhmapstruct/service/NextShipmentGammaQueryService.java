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
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.repository.NextShipmentGammaRepository;
import xyz.jhmapstruct.service.criteria.NextShipmentGammaCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentGammaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentGammaMapper;

/**
 * Service for executing complex queries for {@link NextShipmentGamma} entities in the database.
 * The main input is a {@link NextShipmentGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextShipmentGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextShipmentGammaQueryService extends QueryService<NextShipmentGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentGammaQueryService.class);

    private final NextShipmentGammaRepository nextShipmentGammaRepository;

    private final NextShipmentGammaMapper nextShipmentGammaMapper;

    public NextShipmentGammaQueryService(
        NextShipmentGammaRepository nextShipmentGammaRepository,
        NextShipmentGammaMapper nextShipmentGammaMapper
    ) {
        this.nextShipmentGammaRepository = nextShipmentGammaRepository;
        this.nextShipmentGammaMapper = nextShipmentGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextShipmentGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextShipmentGammaDTO> findByCriteria(NextShipmentGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextShipmentGamma> specification = createSpecification(criteria);
        return nextShipmentGammaRepository.findAll(specification, page).map(nextShipmentGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextShipmentGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextShipmentGamma> specification = createSpecification(criteria);
        return nextShipmentGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextShipmentGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextShipmentGamma> createSpecification(NextShipmentGammaCriteria criteria) {
        Specification<NextShipmentGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextShipmentGamma_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTrackingNumber(), NextShipmentGamma_.trackingNumber)
                );
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), NextShipmentGamma_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), NextShipmentGamma_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextShipmentGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
