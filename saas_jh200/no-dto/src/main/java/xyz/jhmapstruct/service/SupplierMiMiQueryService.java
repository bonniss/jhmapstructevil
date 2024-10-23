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
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.SupplierMiMiRepository;
import xyz.jhmapstruct.service.criteria.SupplierMiMiCriteria;

/**
 * Service for executing complex queries for {@link SupplierMiMi} entities in the database.
 * The main input is a {@link SupplierMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierMiMiQueryService extends QueryService<SupplierMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiMiQueryService.class);

    private final SupplierMiMiRepository supplierMiMiRepository;

    public SupplierMiMiQueryService(SupplierMiMiRepository supplierMiMiRepository) {
        this.supplierMiMiRepository = supplierMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierMiMi> findByCriteria(SupplierMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierMiMi> specification = createSpecification(criteria);
        return supplierMiMiRepository.fetchBagRelationships(supplierMiMiRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierMiMi> specification = createSpecification(criteria);
        return supplierMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierMiMi> createSpecification(SupplierMiMiCriteria criteria) {
        Specification<SupplierMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierMiMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierMiMi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierMiMi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierMiMi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierMiMi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierMiMi_.products, JoinType.LEFT).get(ProductMiMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
