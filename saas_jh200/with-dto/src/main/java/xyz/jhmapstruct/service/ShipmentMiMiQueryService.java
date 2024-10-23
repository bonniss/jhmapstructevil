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
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.criteria.ShipmentMiMiCriteria;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;
import xyz.jhmapstruct.service.mapper.ShipmentMiMiMapper;

/**
 * Service for executing complex queries for {@link ShipmentMiMi} entities in the database.
 * The main input is a {@link ShipmentMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentMiMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentMiMiQueryService extends QueryService<ShipmentMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiMiQueryService.class);

    private final ShipmentMiMiRepository shipmentMiMiRepository;

    private final ShipmentMiMiMapper shipmentMiMiMapper;

    public ShipmentMiMiQueryService(ShipmentMiMiRepository shipmentMiMiRepository, ShipmentMiMiMapper shipmentMiMiMapper) {
        this.shipmentMiMiRepository = shipmentMiMiRepository;
        this.shipmentMiMiMapper = shipmentMiMiMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentMiMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentMiMiDTO> findByCriteria(ShipmentMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentMiMi> specification = createSpecification(criteria);
        return shipmentMiMiRepository.findAll(specification, page).map(shipmentMiMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentMiMi> specification = createSpecification(criteria);
        return shipmentMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentMiMi> createSpecification(ShipmentMiMiCriteria criteria) {
        Specification<ShipmentMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentMiMi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentMiMi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentMiMi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentMiMi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}