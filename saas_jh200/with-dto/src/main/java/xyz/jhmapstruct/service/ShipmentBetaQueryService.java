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
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.repository.ShipmentBetaRepository;
import xyz.jhmapstruct.service.criteria.ShipmentBetaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentBetaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentBetaMapper;

/**
 * Service for executing complex queries for {@link ShipmentBeta} entities in the database.
 * The main input is a {@link ShipmentBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentBetaQueryService extends QueryService<ShipmentBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentBetaQueryService.class);

    private final ShipmentBetaRepository shipmentBetaRepository;

    private final ShipmentBetaMapper shipmentBetaMapper;

    public ShipmentBetaQueryService(ShipmentBetaRepository shipmentBetaRepository, ShipmentBetaMapper shipmentBetaMapper) {
        this.shipmentBetaRepository = shipmentBetaRepository;
        this.shipmentBetaMapper = shipmentBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentBetaDTO> findByCriteria(ShipmentBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentBeta> specification = createSpecification(criteria);
        return shipmentBetaRepository.findAll(specification, page).map(shipmentBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentBeta> specification = createSpecification(criteria);
        return shipmentBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentBeta> createSpecification(ShipmentBetaCriteria criteria) {
        Specification<ShipmentBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentBeta_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentBeta_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentBeta_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentBeta_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
