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
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.repository.NextCategoryViRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryViCriteria;

/**
 * Service for executing complex queries for {@link NextCategoryVi} entities in the database.
 * The main input is a {@link NextCategoryViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryViQueryService extends QueryService<NextCategoryVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViQueryService.class);

    private final NextCategoryViRepository nextCategoryViRepository;

    public NextCategoryViQueryService(NextCategoryViRepository nextCategoryViRepository) {
        this.nextCategoryViRepository = nextCategoryViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryVi> findByCriteria(NextCategoryViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryVi> specification = createSpecification(criteria);
        return nextCategoryViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryVi> specification = createSpecification(criteria);
        return nextCategoryViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryVi> createSpecification(NextCategoryViCriteria criteria) {
        Specification<NextCategoryVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryVi_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
