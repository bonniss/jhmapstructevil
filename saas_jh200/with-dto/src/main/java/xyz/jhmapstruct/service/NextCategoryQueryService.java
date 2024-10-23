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
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.repository.NextCategoryRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryMapper;

/**
 * Service for executing complex queries for {@link NextCategory} entities in the database.
 * The main input is a {@link NextCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryQueryService extends QueryService<NextCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryQueryService.class);

    private final NextCategoryRepository nextCategoryRepository;

    private final NextCategoryMapper nextCategoryMapper;

    public NextCategoryQueryService(NextCategoryRepository nextCategoryRepository, NextCategoryMapper nextCategoryMapper) {
        this.nextCategoryRepository = nextCategoryRepository;
        this.nextCategoryMapper = nextCategoryMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryDTO> findByCriteria(NextCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategory> specification = createSpecification(criteria);
        return nextCategoryRepository.findAll(specification, page).map(nextCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategory> specification = createSpecification(criteria);
        return nextCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategory> createSpecification(NextCategoryCriteria criteria) {
        Specification<NextCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategory_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategory_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextCategory_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
