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
import xyz.jhmapstruct.domain.CategoryGamma;
import xyz.jhmapstruct.repository.CategoryGammaRepository;
import xyz.jhmapstruct.service.criteria.CategoryGammaCriteria;
import xyz.jhmapstruct.service.dto.CategoryGammaDTO;
import xyz.jhmapstruct.service.mapper.CategoryGammaMapper;

/**
 * Service for executing complex queries for {@link CategoryGamma} entities in the database.
 * The main input is a {@link CategoryGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategoryGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryGammaQueryService extends QueryService<CategoryGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryGammaQueryService.class);

    private final CategoryGammaRepository categoryGammaRepository;

    private final CategoryGammaMapper categoryGammaMapper;

    public CategoryGammaQueryService(CategoryGammaRepository categoryGammaRepository, CategoryGammaMapper categoryGammaMapper) {
        this.categoryGammaRepository = categoryGammaRepository;
        this.categoryGammaMapper = categoryGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link CategoryGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryGammaDTO> findByCriteria(CategoryGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryGamma> specification = createSpecification(criteria);
        return categoryGammaRepository.findAll(specification, page).map(categoryGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategoryGamma> specification = createSpecification(criteria);
        return categoryGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryGamma> createSpecification(CategoryGammaCriteria criteria) {
        Specification<CategoryGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryGamma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategoryGamma_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategoryGamma_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategoryGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
