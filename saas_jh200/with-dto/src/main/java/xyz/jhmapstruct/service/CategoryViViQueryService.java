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
import xyz.jhmapstruct.domain.CategoryViVi;
import xyz.jhmapstruct.repository.CategoryViViRepository;
import xyz.jhmapstruct.service.criteria.CategoryViViCriteria;
import xyz.jhmapstruct.service.dto.CategoryViViDTO;
import xyz.jhmapstruct.service.mapper.CategoryViViMapper;

/**
 * Service for executing complex queries for {@link CategoryViVi} entities in the database.
 * The main input is a {@link CategoryViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryViViQueryService extends QueryService<CategoryViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViViQueryService.class);

    private final CategoryViViRepository categoryViViRepository;

    private final CategoryViViMapper categoryViViMapper;

    public CategoryViViQueryService(CategoryViViRepository categoryViViRepository, CategoryViViMapper categoryViViMapper) {
        this.categoryViViRepository = categoryViViRepository;
        this.categoryViViMapper = categoryViViMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoryViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryViViDTO> findByCriteria(CategoryViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryViVi> specification = createSpecification(criteria);
        return categoryViViRepository.findAll(specification, page).map(categoryViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoryViVi> specification = createSpecification(criteria);
        return categoryViViRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryViVi> createSpecification(CategoryViViCriteria criteria) {
        Specification<CategoryViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryViVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryViVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryViVi_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategoryViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
