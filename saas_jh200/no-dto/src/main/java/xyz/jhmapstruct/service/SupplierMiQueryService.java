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
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.criteria.SupplierMiCriteria;

/**
 * Service for executing complex queries for {@link SupplierMi} entities in the database.
 * The main input is a {@link SupplierMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierMiQueryService extends QueryService<SupplierMi> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiQueryService.class);

    private final SupplierMiRepository supplierMiRepository;

    public SupplierMiQueryService(SupplierMiRepository supplierMiRepository) {
        this.supplierMiRepository = supplierMiRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierMi> findByCriteria(SupplierMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierMi> specification = createSpecification(criteria);
        return supplierMiRepository.fetchBagRelationships(supplierMiRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierMi> specification = createSpecification(criteria);
        return supplierMiRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierMi> createSpecification(SupplierMiCriteria criteria) {
        Specification<SupplierMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierMi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierMi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierMi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierMi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierMi_.products, JoinType.LEFT).get(NextProductMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
