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
import xyz.jhmapstruct.domain.Shipment;
import xyz.jhmapstruct.repository.ShipmentRepository;
import xyz.jhmapstruct.service.criteria.ShipmentCriteria;
import xyz.jhmapstruct.service.dto.ShipmentDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMapper;

/**
 * Service for executing complex queries for {@link Shipment} entities in the database.
 * The main input is a {@link ShipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentQueryService extends QueryService<Shipment> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentQueryService.class);

    private final ShipmentRepository shipmentRepository;

    private final ShipmentMapper shipmentMapper;

    public ShipmentQueryService(ShipmentRepository shipmentRepository, ShipmentMapper shipmentMapper) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentDTO> findByCriteria(ShipmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.findAll(specification, page).map(shipmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Shipment> specification = createSpecification(criteria);
        return shipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Shipment> createSpecification(ShipmentCriteria criteria) {
        Specification<Shipment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Shipment_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), Shipment_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), Shipment_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), Shipment_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(Shipment_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
