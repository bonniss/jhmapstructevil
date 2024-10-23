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
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.repository.NextSupplierSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierSigmaCriteria;

/**
 * Service for executing complex queries for {@link NextSupplierSigma} entities in the database.
 * The main input is a {@link NextSupplierSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierSigmaQueryService extends QueryService<NextSupplierSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierSigmaQueryService.class);

    private final NextSupplierSigmaRepository nextSupplierSigmaRepository;

    public NextSupplierSigmaQueryService(NextSupplierSigmaRepository nextSupplierSigmaRepository) {
        this.nextSupplierSigmaRepository = nextSupplierSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierSigma> findByCriteria(NextSupplierSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierSigma> specification = createSpecification(criteria);
        return nextSupplierSigmaRepository.fetchBagRelationships(nextSupplierSigmaRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierSigma> specification = createSpecification(criteria);
        return nextSupplierSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierSigma> createSpecification(NextSupplierSigmaCriteria criteria) {
        Specification<NextSupplierSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierSigma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierSigma_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierSigma_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierSigma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierSigma_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierSigma_.products, JoinType.LEFT).get(NextProductSigma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
