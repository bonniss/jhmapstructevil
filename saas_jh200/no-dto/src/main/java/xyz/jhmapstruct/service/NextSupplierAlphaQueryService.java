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
import xyz.jhmapstruct.domain.NextSupplierAlpha;
import xyz.jhmapstruct.repository.NextSupplierAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierAlphaCriteria;

/**
 * Service for executing complex queries for {@link NextSupplierAlpha} entities in the database.
 * The main input is a {@link NextSupplierAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierAlpha} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierAlphaQueryService extends QueryService<NextSupplierAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierAlphaQueryService.class);

    private final NextSupplierAlphaRepository nextSupplierAlphaRepository;

    public NextSupplierAlphaQueryService(NextSupplierAlphaRepository nextSupplierAlphaRepository) {
        this.nextSupplierAlphaRepository = nextSupplierAlphaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierAlpha} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierAlpha> findByCriteria(NextSupplierAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierAlpha> specification = createSpecification(criteria);
        return nextSupplierAlphaRepository.fetchBagRelationships(nextSupplierAlphaRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierAlpha> specification = createSpecification(criteria);
        return nextSupplierAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierAlpha> createSpecification(NextSupplierAlphaCriteria criteria) {
        Specification<NextSupplierAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierAlpha_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierAlpha_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierAlpha_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierAlpha_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierAlpha_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierAlpha_.products, JoinType.LEFT).get(NextProductAlpha_.id)
                    )
                );
            }
        }
        return specification;
    }
}
