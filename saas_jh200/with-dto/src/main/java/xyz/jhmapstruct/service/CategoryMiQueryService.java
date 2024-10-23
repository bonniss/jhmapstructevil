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
import xyz.jhmapstruct.domain.CategoryMi;
import xyz.jhmapstruct.repository.CategoryMiRepository;
import xyz.jhmapstruct.service.criteria.CategoryMiCriteria;
import xyz.jhmapstruct.service.dto.CategoryMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMapper;

/**
 * Service for executing complex queries for {@link CategoryMi} entities in the database.
 * The main input is a {@link CategoryMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryMiQueryService extends QueryService<CategoryMi> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiQueryService.class);

    private final CategoryMiRepository categoryMiRepository;

    private final CategoryMiMapper categoryMiMapper;

    public CategoryMiQueryService(CategoryMiRepository categoryMiRepository, CategoryMiMapper categoryMiMapper) {
        this.categoryMiRepository = categoryMiRepository;
        this.categoryMiMapper = categoryMiMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoryMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryMiDTO> findByCriteria(CategoryMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryMi> specification = createSpecification(criteria);
        return categoryMiRepository.findAll(specification, page).map(categoryMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoryMi> specification = createSpecification(criteria);
        return categoryMiRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryMi> createSpecification(CategoryMiCriteria criteria) {
        Specification<CategoryMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryMi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryMi_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategoryMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
