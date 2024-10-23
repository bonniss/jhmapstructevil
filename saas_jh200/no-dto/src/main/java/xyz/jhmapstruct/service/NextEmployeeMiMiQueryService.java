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
import xyz.jhmapstruct.domain.NextEmployeeMiMi;
import xyz.jhmapstruct.repository.NextEmployeeMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeMiMiCriteria;

/**
 * Service for executing complex queries for {@link NextEmployeeMiMi} entities in the database.
 * The main input is a {@link NextEmployeeMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeMiMiQueryService extends QueryService<NextEmployeeMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiMiQueryService.class);

    private final NextEmployeeMiMiRepository nextEmployeeMiMiRepository;

    public NextEmployeeMiMiQueryService(NextEmployeeMiMiRepository nextEmployeeMiMiRepository) {
        this.nextEmployeeMiMiRepository = nextEmployeeMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeMiMi> findByCriteria(NextEmployeeMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeMiMi> specification = createSpecification(criteria);
        return nextEmployeeMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeMiMi> specification = createSpecification(criteria);
        return nextEmployeeMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeMiMi> createSpecification(NextEmployeeMiMiCriteria criteria) {
        Specification<NextEmployeeMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeMiMi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeMiMi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeMiMi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeMiMi_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeMiMi_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeMiMi_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
