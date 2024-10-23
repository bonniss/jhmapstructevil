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
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.repository.NextCategoryThetaRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryThetaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryThetaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryThetaMapper;

/**
 * Service for executing complex queries for {@link NextCategoryTheta} entities in the database.
 * The main input is a {@link NextCategoryThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryThetaQueryService extends QueryService<NextCategoryTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryThetaQueryService.class);

    private final NextCategoryThetaRepository nextCategoryThetaRepository;

    private final NextCategoryThetaMapper nextCategoryThetaMapper;

    public NextCategoryThetaQueryService(
        NextCategoryThetaRepository nextCategoryThetaRepository,
        NextCategoryThetaMapper nextCategoryThetaMapper
    ) {
        this.nextCategoryThetaRepository = nextCategoryThetaRepository;
        this.nextCategoryThetaMapper = nextCategoryThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryThetaDTO> findByCriteria(NextCategoryThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryTheta> specification = createSpecification(criteria);
        return nextCategoryThetaRepository.findAll(specification, page).map(nextCategoryThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryTheta> specification = createSpecification(criteria);
        return nextCategoryThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryTheta> createSpecification(NextCategoryThetaCriteria criteria) {
        Specification<NextCategoryTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryTheta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryTheta_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryTheta_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
