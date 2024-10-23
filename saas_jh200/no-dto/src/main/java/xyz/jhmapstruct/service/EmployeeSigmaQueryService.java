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
import xyz.jhmapstruct.domain.EmployeeSigma;
import xyz.jhmapstruct.repository.EmployeeSigmaRepository;
import xyz.jhmapstruct.service.criteria.EmployeeSigmaCriteria;

/**
 * Service for executing complex queries for {@link EmployeeSigma} entities in the database.
 * The main input is a {@link EmployeeSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeSigmaQueryService extends QueryService<EmployeeSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeSigmaQueryService.class);

    private final EmployeeSigmaRepository employeeSigmaRepository;

    public EmployeeSigmaQueryService(EmployeeSigmaRepository employeeSigmaRepository) {
        this.employeeSigmaRepository = employeeSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link EmployeeSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeSigma> findByCriteria(EmployeeSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeSigma> specification = createSpecification(criteria);
        return employeeSigmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeSigma> specification = createSpecification(criteria);
        return employeeSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeSigma> createSpecification(EmployeeSigmaCriteria criteria) {
        Specification<EmployeeSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeSigma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeSigma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeSigma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeSigma_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeSigma_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeSigma_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}