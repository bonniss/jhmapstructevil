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
import xyz.jhmapstruct.domain.NextCategoryBeta;
import xyz.jhmapstruct.repository.NextCategoryBetaRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryBetaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryBetaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryBetaMapper;

/**
 * Service for executing complex queries for {@link NextCategoryBeta} entities in the database.
 * The main input is a {@link NextCategoryBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryBetaQueryService extends QueryService<NextCategoryBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryBetaQueryService.class);

    private final NextCategoryBetaRepository nextCategoryBetaRepository;

    private final NextCategoryBetaMapper nextCategoryBetaMapper;

    public NextCategoryBetaQueryService(
        NextCategoryBetaRepository nextCategoryBetaRepository,
        NextCategoryBetaMapper nextCategoryBetaMapper
    ) {
        this.nextCategoryBetaRepository = nextCategoryBetaRepository;
        this.nextCategoryBetaMapper = nextCategoryBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryBetaDTO> findByCriteria(NextCategoryBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryBeta> specification = createSpecification(criteria);
        return nextCategoryBetaRepository.findAll(specification, page).map(nextCategoryBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryBeta> specification = createSpecification(criteria);
        return nextCategoryBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryBeta> createSpecification(NextCategoryBetaCriteria criteria) {
        Specification<NextCategoryBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryBeta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryBeta_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryBeta_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
