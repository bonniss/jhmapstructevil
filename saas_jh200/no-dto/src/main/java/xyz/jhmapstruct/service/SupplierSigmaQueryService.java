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
import xyz.jhmapstruct.domain.SupplierSigma;
import xyz.jhmapstruct.repository.SupplierSigmaRepository;
import xyz.jhmapstruct.service.criteria.SupplierSigmaCriteria;

/**
 * Service for executing complex queries for {@link SupplierSigma} entities in the database.
 * The main input is a {@link SupplierSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierSigmaQueryService extends QueryService<SupplierSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierSigmaQueryService.class);

    private final SupplierSigmaRepository supplierSigmaRepository;

    public SupplierSigmaQueryService(SupplierSigmaRepository supplierSigmaRepository) {
        this.supplierSigmaRepository = supplierSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link SupplierSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierSigma> findByCriteria(SupplierSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierSigma> specification = createSpecification(criteria);
        return supplierSigmaRepository.fetchBagRelationships(supplierSigmaRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierSigma> specification = createSpecification(criteria);
        return supplierSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierSigma> createSpecification(SupplierSigmaCriteria criteria) {
        Specification<SupplierSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierSigma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierSigma_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierSigma_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierSigma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierSigma_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierSigma_.products, JoinType.LEFT).get(ProductSigma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
