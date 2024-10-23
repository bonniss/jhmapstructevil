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
import xyz.jhmapstruct.domain.NextSupplierViVi;
import xyz.jhmapstruct.repository.NextSupplierViViRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierViViCriteria;

/**
 * Service for executing complex queries for {@link NextSupplierViVi} entities in the database.
 * The main input is a {@link NextSupplierViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierViViQueryService extends QueryService<NextSupplierViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViViQueryService.class);

    private final NextSupplierViViRepository nextSupplierViViRepository;

    public NextSupplierViViQueryService(NextSupplierViViRepository nextSupplierViViRepository) {
        this.nextSupplierViViRepository = nextSupplierViViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierViVi> findByCriteria(NextSupplierViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierViVi> specification = createSpecification(criteria);
        return nextSupplierViViRepository.fetchBagRelationships(nextSupplierViViRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierViVi> specification = createSpecification(criteria);
        return nextSupplierViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierViVi> createSpecification(NextSupplierViViCriteria criteria) {
        Specification<NextSupplierViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierViVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierViVi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierViVi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierViVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierViVi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierViVi_.products, JoinType.LEFT).get(NextProductViVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
