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
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.repository.NextCategoryAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryAlphaMapper;

/**
 * Service for executing complex queries for {@link NextCategoryAlpha} entities in the database.
 * The main input is a {@link NextCategoryAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryAlphaQueryService extends QueryService<NextCategoryAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryAlphaQueryService.class);

    private final NextCategoryAlphaRepository nextCategoryAlphaRepository;

    private final NextCategoryAlphaMapper nextCategoryAlphaMapper;

    public NextCategoryAlphaQueryService(
        NextCategoryAlphaRepository nextCategoryAlphaRepository,
        NextCategoryAlphaMapper nextCategoryAlphaMapper
    ) {
        this.nextCategoryAlphaRepository = nextCategoryAlphaRepository;
        this.nextCategoryAlphaMapper = nextCategoryAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryAlphaDTO> findByCriteria(NextCategoryAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryAlpha> specification = createSpecification(criteria);
        return nextCategoryAlphaRepository.findAll(specification, page).map(nextCategoryAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryAlpha> specification = createSpecification(criteria);
        return nextCategoryAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryAlpha> createSpecification(NextCategoryAlphaCriteria criteria) {
        Specification<NextCategoryAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryAlpha_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryAlpha_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryAlpha_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
