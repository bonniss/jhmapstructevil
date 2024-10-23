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
import xyz.jhmapstruct.domain.SupplierBeta;
import xyz.jhmapstruct.repository.SupplierBetaRepository;
import xyz.jhmapstruct.service.criteria.SupplierBetaCriteria;

/**
 * Service for executing complex queries for {@link SupplierBeta} entities in the database.
 * The main input is a {@link SupplierBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierBetaQueryService extends QueryService<SupplierBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierBetaQueryService.class);

    private final SupplierBetaRepository supplierBetaRepository;

    public SupplierBetaQueryService(SupplierBetaRepository supplierBetaRepository) {
        this.supplierBetaRepository = supplierBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierBeta> findByCriteria(SupplierBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierBeta> specification = createSpecification(criteria);
        return supplierBetaRepository.fetchBagRelationships(supplierBetaRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierBeta> specification = createSpecification(criteria);
        return supplierBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierBeta> createSpecification(SupplierBetaCriteria criteria) {
        Specification<SupplierBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierBeta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierBeta_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierBeta_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierBeta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierBeta_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierBeta_.products, JoinType.LEFT).get(ProductBeta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
