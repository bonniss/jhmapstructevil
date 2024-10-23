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
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.repository.NextCustomerGammaRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerGammaCriteria;

/**
 * Service for executing complex queries for {@link NextCustomerGamma} entities in the database.
 * The main input is a {@link NextCustomerGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerGammaQueryService extends QueryService<NextCustomerGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerGammaQueryService.class);

    private final NextCustomerGammaRepository nextCustomerGammaRepository;

    public NextCustomerGammaQueryService(NextCustomerGammaRepository nextCustomerGammaRepository) {
        this.nextCustomerGammaRepository = nextCustomerGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerGamma> findByCriteria(NextCustomerGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerGamma> specification = createSpecification(criteria);
        return nextCustomerGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerGamma> specification = createSpecification(criteria);
        return nextCustomerGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerGamma> createSpecification(NextCustomerGammaCriteria criteria) {
        Specification<NextCustomerGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerGamma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerGamma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerGamma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerGamma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerGamma_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root ->
                        root.join(NextCustomerGamma_.orders, JoinType.LEFT).get(NextOrderGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
