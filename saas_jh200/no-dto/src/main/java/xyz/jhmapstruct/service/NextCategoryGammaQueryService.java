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
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.repository.NextCategoryGammaRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryGammaCriteria;

/**
 * Service for executing complex queries for {@link NextCategoryGamma} entities in the database.
 * The main input is a {@link NextCategoryGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryGammaQueryService extends QueryService<NextCategoryGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryGammaQueryService.class);

    private final NextCategoryGammaRepository nextCategoryGammaRepository;

    public NextCategoryGammaQueryService(NextCategoryGammaRepository nextCategoryGammaRepository) {
        this.nextCategoryGammaRepository = nextCategoryGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryGamma> findByCriteria(NextCategoryGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryGamma> specification = createSpecification(criteria);
        return nextCategoryGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryGamma> specification = createSpecification(criteria);
        return nextCategoryGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryGamma> createSpecification(NextCategoryGammaCriteria criteria) {
        Specification<NextCategoryGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryGamma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryGamma_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryGamma_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
