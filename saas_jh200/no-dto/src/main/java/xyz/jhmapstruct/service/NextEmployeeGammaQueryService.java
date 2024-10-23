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
import xyz.jhmapstruct.domain.NextEmployeeGamma;
import xyz.jhmapstruct.repository.NextEmployeeGammaRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeGammaCriteria;

/**
 * Service for executing complex queries for {@link NextEmployeeGamma} entities in the database.
 * The main input is a {@link NextEmployeeGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeGammaQueryService extends QueryService<NextEmployeeGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeGammaQueryService.class);

    private final NextEmployeeGammaRepository nextEmployeeGammaRepository;

    public NextEmployeeGammaQueryService(NextEmployeeGammaRepository nextEmployeeGammaRepository) {
        this.nextEmployeeGammaRepository = nextEmployeeGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeGamma> findByCriteria(NextEmployeeGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeGamma> specification = createSpecification(criteria);
        return nextEmployeeGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeGamma> specification = createSpecification(criteria);
        return nextEmployeeGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeGamma> createSpecification(NextEmployeeGammaCriteria criteria) {
        Specification<NextEmployeeGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeGamma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeGamma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeGamma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeGamma_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeGamma_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeGamma_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
