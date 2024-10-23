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
import xyz.jhmapstruct.domain.NextReview;
import xyz.jhmapstruct.repository.NextReviewRepository;
import xyz.jhmapstruct.service.criteria.NextReviewCriteria;
import xyz.jhmapstruct.service.dto.NextReviewDTO;
import xyz.jhmapstruct.service.mapper.NextReviewMapper;

/**
 * Service for executing complex queries for {@link NextReview} entities in the database.
 * The main input is a {@link NextReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewQueryService extends QueryService<NextReview> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewQueryService.class);

    private final NextReviewRepository nextReviewRepository;

    private final NextReviewMapper nextReviewMapper;

    public NextReviewQueryService(NextReviewRepository nextReviewRepository, NextReviewMapper nextReviewMapper) {
        this.nextReviewRepository = nextReviewRepository;
        this.nextReviewMapper = nextReviewMapper;
    }

    /**
     * Return a {@link Page} of {@link NextReviewDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewDTO> findByCriteria(NextReviewCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReview> specification = createSpecification(criteria);
        return nextReviewRepository.findAll(specification, page).map(nextReviewMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReview> specification = createSpecification(criteria);
        return nextReviewRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReview> createSpecification(NextReviewCriteria criteria) {
        Specification<NextReview> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReview_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReview_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReview_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(NextReview_.product, JoinType.LEFT).get(NextProduct_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextReview_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
