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
import xyz.jhmapstruct.domain.NextEmployeeMi;
import xyz.jhmapstruct.repository.NextEmployeeMiRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeMiCriteria;

/**
 * Service for executing complex queries for {@link NextEmployeeMi} entities in the database.
 * The main input is a {@link NextEmployeeMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeMiQueryService extends QueryService<NextEmployeeMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiQueryService.class);

    private final NextEmployeeMiRepository nextEmployeeMiRepository;

    public NextEmployeeMiQueryService(NextEmployeeMiRepository nextEmployeeMiRepository) {
        this.nextEmployeeMiRepository = nextEmployeeMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeMi> findByCriteria(NextEmployeeMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeMi> specification = createSpecification(criteria);
        return nextEmployeeMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeMi> specification = createSpecification(criteria);
        return nextEmployeeMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeMi> createSpecification(NextEmployeeMiCriteria criteria) {
        Specification<NextEmployeeMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeMi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeMi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeMi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeMi_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeMi_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeMi_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
