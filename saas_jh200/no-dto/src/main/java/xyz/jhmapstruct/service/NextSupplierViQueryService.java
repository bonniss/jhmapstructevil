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
import xyz.jhmapstruct.domain.NextSupplierVi;
import xyz.jhmapstruct.repository.NextSupplierViRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierViCriteria;

/**
 * Service for executing complex queries for {@link NextSupplierVi} entities in the database.
 * The main input is a {@link NextSupplierViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierViQueryService extends QueryService<NextSupplierVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViQueryService.class);

    private final NextSupplierViRepository nextSupplierViRepository;

    public NextSupplierViQueryService(NextSupplierViRepository nextSupplierViRepository) {
        this.nextSupplierViRepository = nextSupplierViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierVi> findByCriteria(NextSupplierViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierVi> specification = createSpecification(criteria);
        return nextSupplierViRepository.fetchBagRelationships(nextSupplierViRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierVi> specification = createSpecification(criteria);
        return nextSupplierViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierVi> createSpecification(NextSupplierViCriteria criteria) {
        Specification<NextSupplierVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierVi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierVi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierVi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierVi_.products, JoinType.LEFT).get(NextProductVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
