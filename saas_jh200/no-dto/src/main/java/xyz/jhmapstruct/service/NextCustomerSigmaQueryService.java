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
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.repository.NextCustomerSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerSigmaCriteria;

/**
 * Service for executing complex queries for {@link NextCustomerSigma} entities in the database.
 * The main input is a {@link NextCustomerSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerSigmaQueryService extends QueryService<NextCustomerSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerSigmaQueryService.class);

    private final NextCustomerSigmaRepository nextCustomerSigmaRepository;

    public NextCustomerSigmaQueryService(NextCustomerSigmaRepository nextCustomerSigmaRepository) {
        this.nextCustomerSigmaRepository = nextCustomerSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerSigma> findByCriteria(NextCustomerSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerSigma> specification = createSpecification(criteria);
        return nextCustomerSigmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerSigma> specification = createSpecification(criteria);
        return nextCustomerSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerSigma> createSpecification(NextCustomerSigmaCriteria criteria) {
        Specification<NextCustomerSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerSigma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerSigma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerSigma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerSigma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerSigma_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root ->
                        root.join(NextCustomerSigma_.orders, JoinType.LEFT).get(NextOrderSigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
