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
import xyz.jhmapstruct.domain.ShipmentTheta;
import xyz.jhmapstruct.repository.ShipmentThetaRepository;
import xyz.jhmapstruct.service.criteria.ShipmentThetaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentThetaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentThetaMapper;

/**
 * Service for executing complex queries for {@link ShipmentTheta} entities in the database.
 * The main input is a {@link ShipmentThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentThetaQueryService extends QueryService<ShipmentTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentThetaQueryService.class);

    private final ShipmentThetaRepository shipmentThetaRepository;

    private final ShipmentThetaMapper shipmentThetaMapper;

    public ShipmentThetaQueryService(ShipmentThetaRepository shipmentThetaRepository, ShipmentThetaMapper shipmentThetaMapper) {
        this.shipmentThetaRepository = shipmentThetaRepository;
        this.shipmentThetaMapper = shipmentThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentThetaDTO> findByCriteria(ShipmentThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentTheta> specification = createSpecification(criteria);
        return shipmentThetaRepository.findAll(specification, page).map(shipmentThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentTheta> specification = createSpecification(criteria);
        return shipmentThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentTheta> createSpecification(ShipmentThetaCriteria criteria) {
        Specification<ShipmentTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentTheta_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentTheta_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentTheta_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentTheta_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
