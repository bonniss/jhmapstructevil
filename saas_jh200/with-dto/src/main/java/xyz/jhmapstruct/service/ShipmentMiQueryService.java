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
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;
import xyz.jhmapstruct.service.criteria.ShipmentMiCriteria;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMapper;

/**
 * Service for executing complex queries for {@link ShipmentMi} entities in the database.
 * The main input is a {@link ShipmentMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentMiQueryService extends QueryService<ShipmentMi> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiQueryService.class);

    private final ShipmentMiRepository shipmentMiRepository;

    private final ShipmentMiMapper shipmentMiMapper;

    public ShipmentMiQueryService(ShipmentMiRepository shipmentMiRepository, ShipmentMiMapper shipmentMiMapper) {
        this.shipmentMiRepository = shipmentMiRepository;
        this.shipmentMiMapper = shipmentMiMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentMiDTO> findByCriteria(ShipmentMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentMi> specification = createSpecification(criteria);
        return shipmentMiRepository.findAll(specification, page).map(shipmentMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentMi> specification = createSpecification(criteria);
        return shipmentMiRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentMi> createSpecification(ShipmentMiCriteria criteria) {
        Specification<ShipmentMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentMi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentMi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentMi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentMi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
