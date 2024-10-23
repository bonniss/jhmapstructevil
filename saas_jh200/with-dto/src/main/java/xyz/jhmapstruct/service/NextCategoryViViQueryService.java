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
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.repository.NextCategoryViViRepository;
import xyz.jhmapstruct.service.criteria.NextCategoryViViCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryViViDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryViViMapper;

/**
 * Service for executing complex queries for {@link NextCategoryViVi} entities in the database.
 * The main input is a {@link NextCategoryViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCategoryViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCategoryViViQueryService extends QueryService<NextCategoryViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViViQueryService.class);

    private final NextCategoryViViRepository nextCategoryViViRepository;

    private final NextCategoryViViMapper nextCategoryViViMapper;

    public NextCategoryViViQueryService(
        NextCategoryViViRepository nextCategoryViViRepository,
        NextCategoryViViMapper nextCategoryViViMapper
    ) {
        this.nextCategoryViViRepository = nextCategoryViViRepository;
        this.nextCategoryViViMapper = nextCategoryViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCategoryViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCategoryViViDTO> findByCriteria(NextCategoryViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCategoryViVi> specification = createSpecification(criteria);
        return nextCategoryViViRepository.findAll(specification, page).map(nextCategoryViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCategoryViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCategoryViVi> specification = createSpecification(criteria);
        return nextCategoryViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCategoryViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCategoryViVi> createSpecification(NextCategoryViViCriteria criteria) {
        Specification<NextCategoryViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCategoryViVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextCategoryViVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NextCategoryViVi_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCategoryViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
