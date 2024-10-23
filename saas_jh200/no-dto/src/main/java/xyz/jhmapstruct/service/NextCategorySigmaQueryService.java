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
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.repository.NextCategorySigmaRepository;
import xyz.jhmapstruct.service.criteria.NextCategorySigmaCriteria;

/**
 * Service for executing complex queries for {@link NextCategorySigma} entities in the database.
 * The main input is a {@link NextCategorySigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategorySigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategorySigmaQueryService extends QueryService<NextCategorySigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategorySigmaQueryService.class);

    private final NextCategorySigmaRepository nextCategorySigmaRepository;

    public NextCategorySigmaQueryService(NextCategorySigmaRepository nextCategorySigmaRepository) {
        this.nextCategorySigmaRepository = nextCategorySigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCategorySigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategorySigma> findByCriteria(NextCategorySigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategorySigma> specification = createSpecification(criteria);
        return nextCategorySigmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategorySigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategorySigma> specification = createSpecification(criteria);
        return nextCategorySigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategorySigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategorySigma> createSpecification(NextCategorySigmaCriteria criteria) {
        Specification<NextCategorySigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategorySigma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategorySigma_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategorySigma_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategorySigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
