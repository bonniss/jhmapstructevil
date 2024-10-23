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
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.repository.SupplierThetaRepository;
import xyz.jhmapstruct.service.criteria.SupplierThetaCriteria;

/**
 * Service for executing complex queries for {@link SupplierTheta} entities in the database.
 * The main input is a {@link SupplierThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierThetaQueryService extends QueryService<SupplierTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierThetaQueryService.class);

    private final SupplierThetaRepository supplierThetaRepository;

    public SupplierThetaQueryService(SupplierThetaRepository supplierThetaRepository) {
        this.supplierThetaRepository = supplierThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierTheta> findByCriteria(SupplierThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierTheta> specification = createSpecification(criteria);
        return supplierThetaRepository.fetchBagRelationships(supplierThetaRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierTheta> specification = createSpecification(criteria);
        return supplierThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierTheta> createSpecification(SupplierThetaCriteria criteria) {
        Specification<SupplierTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierTheta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierTheta_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierTheta_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierTheta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierTheta_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierTheta_.products, JoinType.LEFT).get(ProductTheta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
