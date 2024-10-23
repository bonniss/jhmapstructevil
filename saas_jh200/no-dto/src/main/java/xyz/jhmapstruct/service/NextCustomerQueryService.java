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
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.repository.NextCustomerRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerCriteria;

/**
 * Service for executing complex queries for {@link NextCustomer} entities in the database.
 * The main input is a {@link NextCustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerQueryService extends QueryService<NextCustomer> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerQueryService.class);

    private final NextCustomerRepository nextCustomerRepository;

    public NextCustomerQueryService(NextCustomerRepository nextCustomerRepository) {
        this.nextCustomerRepository = nextCustomerRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCustomer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomer> findByCriteria(NextCustomerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomer> specification = createSpecification(criteria);
        return nextCustomerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomer> specification = createSpecification(criteria);
        return nextCustomerRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomer> createSpecification(NextCustomerCriteria criteria) {
        Specification<NextCustomer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomer_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomer_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomer_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomer_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomer_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(NextCustomer_.orders, JoinType.LEFT).get(NextOrder_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextCustomer_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
