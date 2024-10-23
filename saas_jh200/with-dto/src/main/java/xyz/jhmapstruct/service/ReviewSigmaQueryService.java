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
import xyz.jhmapstruct.domain.ReviewSigma;
import xyz.jhmapstruct.repository.ReviewSigmaRepository;
import xyz.jhmapstruct.service.criteria.ReviewSigmaCriteria;
import xyz.jhmapstruct.service.dto.ReviewSigmaDTO;
import xyz.jhmapstruct.service.mapper.ReviewSigmaMapper;

/**
 * Service for executing complex queries for {@link ReviewSigma} entities in the database.
 * The main input is a {@link ReviewSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewSigmaQueryService extends QueryService<ReviewSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewSigmaQueryService.class);

    private final ReviewSigmaRepository reviewSigmaRepository;

    private final ReviewSigmaMapper reviewSigmaMapper;

    public ReviewSigmaQueryService(ReviewSigmaRepository reviewSigmaRepository, ReviewSigmaMapper reviewSigmaMapper) {
        this.reviewSigmaRepository = reviewSigmaRepository;
        this.reviewSigmaMapper = reviewSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link ReviewSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewSigmaDTO> findByCriteria(ReviewSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewSigma> specification = createSpecification(criteria);
        return reviewSigmaRepository.findAll(specification, page).map(reviewSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewSigma> specification = createSpecification(criteria);
        return reviewSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewSigma> createSpecification(ReviewSigmaCriteria criteria) {
        Specification<ReviewSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewSigma_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewSigma_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewSigma_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewSigma_.product, JoinType.LEFT).get(ProductSigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
