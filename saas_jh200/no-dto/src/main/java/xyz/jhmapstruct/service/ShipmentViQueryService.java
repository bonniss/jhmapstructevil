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
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;
import xyz.jhmapstruct.service.criteria.ShipmentViCriteria;

/**
 * Service for executing complex queries for {@link ShipmentVi} entities in the database.
 * The main input is a {@link ShipmentViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShipmentVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShipmentViQueryService extends QueryService<ShipmentVi> {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViQueryService.class);

    private final ShipmentViRepository shipmentViRepository;

    public ShipmentViQueryService(ShipmentViRepository shipmentViRepository) {
        this.shipmentViRepository = shipmentViRepository;
    }

    /**
     * Return a {@link Page} of {@link ShipmentVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShipmentVi> findByCriteria(ShipmentViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShipmentVi> specification = createSpecification(criteria);
        return shipmentViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShipmentViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ShipmentVi> specification = createSpecification(criteria);
        return shipmentViRepository.count(specification);
    }

    /**
     * Function to convert {@link ShipmentViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShipmentVi> createSpecification(ShipmentViCriteria criteria) {
        Specification<ShipmentVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShipmentVi_.id));
            }
            if (criteria.getTrackingNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTrackingNumber(), ShipmentVi_.trackingNumber));
            }
            if (criteria.getShippedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShippedDate(), ShipmentVi_.shippedDate));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), ShipmentVi_.deliveryDate));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ShipmentVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
