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
import xyz.jhmapstruct.domain.CategoryMiMi;
import xyz.jhmapstruct.repository.CategoryMiMiRepository;
import xyz.jhmapstruct.service.criteria.CategoryMiMiCriteria;
import xyz.jhmapstruct.service.dto.CategoryMiMiDTO;
import xyz.jhmapstruct.service.mapper.CategoryMiMiMapper;

/**
 * Service for executing complex queries for {@link CategoryMiMi} entities in the database.
 * The main input is a {@link CategoryMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryMiMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryMiMiQueryService extends QueryService<CategoryMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryMiMiQueryService.class);

    private final CategoryMiMiRepository categoryMiMiRepository;

    private final CategoryMiMiMapper categoryMiMiMapper;

    public CategoryMiMiQueryService(CategoryMiMiRepository categoryMiMiRepository, CategoryMiMiMapper categoryMiMiMapper) {
        this.categoryMiMiRepository = categoryMiMiRepository;
        this.categoryMiMiMapper = categoryMiMiMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoryMiMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryMiMiDTO> findByCriteria(CategoryMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryMiMi> specification = createSpecification(criteria);
        return categoryMiMiRepository.findAll(specification, page).map(categoryMiMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoryMiMi> specification = createSpecification(criteria);
        return categoryMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryMiMi> createSpecification(CategoryMiMiCriteria criteria) {
        Specification<CategoryMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryMiMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryMiMi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryMiMi_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategoryMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
