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
import xyz.jhmapstruct.domain.NextEmployee;
import xyz.jhmapstruct.repository.NextEmployeeRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeCriteria;

/**
 * Service for executing complex queries for {@link NextEmployee} entities in the database.
 * The main input is a {@link NextEmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployee} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeQueryService extends QueryService<NextEmployee> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeQueryService.class);

    private final NextEmployeeRepository nextEmployeeRepository;

    public NextEmployeeQueryService(NextEmployeeRepository nextEmployeeRepository) {
        this.nextEmployeeRepository = nextEmployeeRepository;
    }

    /**
     * Return a {@link Page} of {@link NextEmployee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployee> findByCriteria(NextEmployeeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployee> specification = createSpecification(criteria);
        return nextEmployeeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployee> specification = createSpecification(criteria);
        return nextEmployeeRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployee> createSpecification(NextEmployeeCriteria criteria) {
        Specification<NextEmployee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployee_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployee_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployee_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployee_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployee_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployee_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextEmployee_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
