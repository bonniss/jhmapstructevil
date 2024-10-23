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
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.repository.ShipmentAlphaRepository;
import xyz.jhmapstruct.service.criteria.ShipmentAlphaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentAlphaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentAlphaMapper;

/**
 * Service for executing complex queries for {@link ShipmentAlpha} entities in the database.
 * The main input is a {@link ShipmentAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentAlphaQueryService extends QueryService<ShipmentAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAlphaQueryService.class);

    private final ShipmentAlphaRepository shipmentAlphaRepository;

    private final ShipmentAlphaMapper shipmentAlphaMapper;

    public ShipmentAlphaQueryService(ShipmentAlphaRepository shipmentAlphaRepository, ShipmentAlphaMapper shipmentAlphaMapper) {
        this.shipmentAlphaRepository = shipmentAlphaRepository;
        this.shipmentAlphaMapper = shipmentAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentAlphaDTO> findByCriteria(ShipmentAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentAlpha> specification = createSpecification(criteria);
        return shipmentAlphaRepository.findAll(specification, page).map(shipmentAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentAlpha> specification = createSpecification(criteria);
        return shipmentAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentAlpha> createSpecification(ShipmentAlphaCriteria criteria) {
        Specification<ShipmentAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentAlpha_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentAlpha_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentAlpha_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentAlpha_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
