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
import xyz.jhmapstruct.domain.NextReviewViVi;
import xyz.jhmapstruct.repository.NextReviewViViRepository;
import xyz.jhmapstruct.service.criteria.NextReviewViViCriteria;
import xyz.jhmapstruct.service.dto.NextReviewViViDTO;
import xyz.jhmapstruct.service.mapper.NextReviewViViMapper;

/**
 * Service for executing complex queries for {@link NextReviewViVi} entities in the database.
 * The main input is a {@link NextReviewViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewViViQueryService extends QueryService<NextReviewViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViViQueryService.class);

    private final NextReviewViViRepository nextReviewViViRepository;

    private final NextReviewViViMapper nextReviewViViMapper;

    public NextReviewViViQueryService(NextReviewViViRepository nextReviewViViRepository, NextReviewViViMapper nextReviewViViMapper) {
        this.nextReviewViViRepository = nextReviewViViRepository;
        this.nextReviewViViMapper = nextReviewViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextReviewViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewViViDTO> findByCriteria(NextReviewViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewViVi> specification = createSpecification(criteria);
        return nextReviewViViRepository.findAll(specification, page).map(nextReviewViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewViVi> specification = createSpecification(criteria);
        return nextReviewViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewViVi> createSpecification(NextReviewViViCriteria criteria) {
        Specification<NextReviewViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewViVi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewViVi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewViVi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewViVi_.product, JoinType.LEFT).get(NextProductViVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
