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
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.repository.NextCustomerViRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerViCriteria;

/**
 * Service for executing complex queries for {@link NextCustomerVi} entities in the database.
 * The main input is a {@link NextCustomerViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerViQueryService extends QueryService<NextCustomerVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViQueryService.class);

    private final NextCustomerViRepository nextCustomerViRepository;

    public NextCustomerViQueryService(NextCustomerViRepository nextCustomerViRepository) {
        this.nextCustomerViRepository = nextCustomerViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerVi> findByCriteria(NextCustomerViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerVi> specification = createSpecification(criteria);
        return nextCustomerViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerVi> specification = createSpecification(criteria);
        return nextCustomerViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerVi> createSpecification(NextCustomerViCriteria criteria) {
        Specification<NextCustomerVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerVi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerVi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerVi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerVi_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(NextCustomerVi_.orders, JoinType.LEFT).get(NextOrderVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
