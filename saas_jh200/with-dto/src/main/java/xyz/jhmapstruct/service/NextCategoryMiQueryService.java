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
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.repository.NextCategoryMiRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryMiCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryMiDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryMiMapper;

/**
 * Service for executing complex queries for {@link NextCategoryMi} entities in the database.
 * The main input is a {@link NextCategoryMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryMiQueryService extends QueryService<NextCategoryMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiQueryService.class);

    private final NextCategoryMiRepository nextCategoryMiRepository;

    private final NextCategoryMiMapper nextCategoryMiMapper;

    public NextCategoryMiQueryService(NextCategoryMiRepository nextCategoryMiRepository, NextCategoryMiMapper nextCategoryMiMapper) {
        this.nextCategoryMiRepository = nextCategoryMiRepository;
        this.nextCategoryMiMapper = nextCategoryMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryMiDTO> findByCriteria(NextCategoryMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryMi> specification = createSpecification(criteria);
        return nextCategoryMiRepository.findAll(specification, page).map(nextCategoryMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryMi> specification = createSpecification(criteria);
        return nextCategoryMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryMi> createSpecification(NextCategoryMiCriteria criteria) {
        Specification<NextCategoryMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryMi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryMi_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
