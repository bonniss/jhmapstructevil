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
import xyz.jhmapstruct.domain.CategoryAlpha;
import xyz.jhmapstruct.repository.CategoryAlphaRepository;
import xyz.jhmapstruct.service.criteria.CategoryAlphaCriteria;
import xyz.jhmapstruct.service.dto.CategoryAlphaDTO;
import xyz.jhmapstruct.service.mapper.CategoryAlphaMapper;

/**
 * Service for executing complex queries for {@link CategoryAlpha} entities in the database.
 * The main input is a {@link CategoryAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryAlphaQueryService extends QueryService<CategoryAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryAlphaQueryService.class);

    private final CategoryAlphaRepository categoryAlphaRepository;

    private final CategoryAlphaMapper categoryAlphaMapper;

    public CategoryAlphaQueryService(CategoryAlphaRepository categoryAlphaRepository, CategoryAlphaMapper categoryAlphaMapper) {
        this.categoryAlphaRepository = categoryAlphaRepository;
        this.categoryAlphaMapper = categoryAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoryAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryAlphaDTO> findByCriteria(CategoryAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryAlpha> specification = createSpecification(criteria);
        return categoryAlphaRepository.findAll(specification, page).map(categoryAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoryAlpha> specification = createSpecification(criteria);
        return categoryAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryAlpha> createSpecification(CategoryAlphaCriteria criteria) {
        Specification<CategoryAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryAlpha_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryAlpha_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryAlpha_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategoryAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
