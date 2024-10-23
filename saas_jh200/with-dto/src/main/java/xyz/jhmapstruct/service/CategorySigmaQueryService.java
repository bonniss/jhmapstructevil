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
import xyz.jhmapstruct.domain.CategorySigma;
import xyz.jhmapstruct.repository.CategorySigmaRepository;
import xyz.jhmapstruct.service.criteria.CategorySigmaCriteria;
import xyz.jhmapstruct.service.dto.CategorySigmaDTO;
import xyz.jhmapstruct.service.mapper.CategorySigmaMapper;

/**
 * Service for executing complex queries for {@link CategorySigma} entities in the database.
 * The main input is a {@link CategorySigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CategorySigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategorySigmaQueryService extends QueryService<CategorySigma> {

    private static final Logger LOG = LoggerFactory.getLogger(CategorySigmaQueryService.class);

    private final CategorySigmaRepository categorySigmaRepository;

    private final CategorySigmaMapper categorySigmaMapper;

    public CategorySigmaQueryService(CategorySigmaRepository categorySigmaRepository, CategorySigmaMapper categorySigmaMapper) {
        this.categorySigmaRepository = categorySigmaRepository;
        this.categorySigmaMapper = categorySigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link CategorySigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategorySigmaDTO> findByCriteria(CategorySigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategorySigma> specification = createSpecification(criteria);
        return categorySigmaRepository.findAll(specification, page).map(categorySigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategorySigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CategorySigma> specification = createSpecification(criteria);
        return categorySigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link CategorySigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategorySigma> createSpecification(CategorySigmaCriteria criteria) {
        Specification<CategorySigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategorySigma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CategorySigma_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CategorySigma_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CategorySigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
