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
import xyz.jhmapstruct.domain.ShipmentGamma;
import xyz.jhmapstruct.repository.ShipmentGammaRepository;
import xyz.jhmapstruct.service.criteria.ShipmentGammaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentGammaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentGammaMapper;

/**
 * Service for executing complex queries for {@link ShipmentGamma} entities in the database.
 * The main input is a {@link ShipmentGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentGammaQueryService extends QueryService<ShipmentGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentGammaQueryService.class);

    private final ShipmentGammaRepository shipmentGammaRepository;

    private final ShipmentGammaMapper shipmentGammaMapper;

    public ShipmentGammaQueryService(ShipmentGammaRepository shipmentGammaRepository, ShipmentGammaMapper shipmentGammaMapper) {
        this.shipmentGammaRepository = shipmentGammaRepository;
        this.shipmentGammaMapper = shipmentGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link ShipmentGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentGammaDTO> findByCriteria(ShipmentGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentGamma> specification = createSpecification(criteria);
        return shipmentGammaRepository.findAll(specification, page).map(shipmentGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentGamma> specification = createSpecification(criteria);
        return shipmentGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentGamma> createSpecification(ShipmentGammaCriteria criteria) {
        Specification<ShipmentGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentGamma_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentGamma_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentGamma_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentGamma_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
