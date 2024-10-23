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
import xyz.jhmapstruct.domain.CategoryBeta;
import xyz.jhmapstruct.repository.CategoryBetaRepository;
import xyz.jhmapstruct.service.criteria.CategoryBetaCriteria;

/**
 * Service for executing complex queries for {@link CategoryBeta} entities in the database.
 * The main input is a {@link CategoryBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryBetaQueryService extends QueryService<CategoryBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryBetaQueryService.class);

    private final CategoryBetaRepository categoryBetaRepository;

    public CategoryBetaQueryService(CategoryBetaRepository categoryBetaRepository) {
        this.categoryBetaRepository = categoryBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link CategoryBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryBeta> findByCriteria(CategoryBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryBeta> specification = createSpecification(criteria);
        return categoryBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoryBeta> specification = createSpecification(criteria);
        return categoryBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryBeta> createSpecification(CategoryBetaCriteria criteria) {
        Specification<CategoryBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryBeta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryBeta_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryBeta_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategoryBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
