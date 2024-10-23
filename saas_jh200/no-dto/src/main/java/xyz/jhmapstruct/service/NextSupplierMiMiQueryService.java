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
import xyz.jhmapstruct.domain.NextSupplierMiMi;
import xyz.jhmapstruct.repository.NextSupplierMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierMiMiCriteria;

/**
 * Service for executing complex queries for {@link NextSupplierMiMi} entities in the database.
 * The main input is a {@link NextSupplierMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierMiMiQueryService extends QueryService<NextSupplierMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiMiQueryService.class);

    private final NextSupplierMiMiRepository nextSupplierMiMiRepository;

    public NextSupplierMiMiQueryService(NextSupplierMiMiRepository nextSupplierMiMiRepository) {
        this.nextSupplierMiMiRepository = nextSupplierMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierMiMi> findByCriteria(NextSupplierMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierMiMi> specification = createSpecification(criteria);
        return nextSupplierMiMiRepository.fetchBagRelationships(nextSupplierMiMiRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierMiMi> specification = createSpecification(criteria);
        return nextSupplierMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierMiMi> createSpecification(NextSupplierMiMiCriteria criteria) {
        Specification<NextSupplierMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierMiMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierMiMi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierMiMi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierMiMi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierMiMi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierMiMi_.products, JoinType.LEFT).get(NextProductMiMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
