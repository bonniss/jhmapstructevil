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
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.repository.SupplierViViRepository;
import xyz.jhmapstruct.service.criteria.SupplierViViCriteria;

/**
 * Service for executing complex queries for {@link SupplierViVi} entities in the database.
 * The main input is a {@link SupplierViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierViViQueryService extends QueryService<SupplierViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViViQueryService.class);

    private final SupplierViViRepository supplierViViRepository;

    public SupplierViViQueryService(SupplierViViRepository supplierViViRepository) {
        this.supplierViViRepository = supplierViViRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierViVi> findByCriteria(SupplierViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierViVi> specification = createSpecification(criteria);
        return supplierViViRepository.fetchBagRelationships(supplierViViRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierViVi> specification = createSpecification(criteria);
        return supplierViViRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierViVi> createSpecification(SupplierViViCriteria criteria) {
        Specification<SupplierViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierViVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierViVi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierViVi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierViVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierViVi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierViVi_.products, JoinType.LEFT).get(ProductViVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
