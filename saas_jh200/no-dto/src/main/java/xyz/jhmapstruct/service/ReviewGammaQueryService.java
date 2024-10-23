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
import xyz.jhmapstruct.domain.ReviewGamma;
import xyz.jhmapstruct.repository.ReviewGammaRepository;
import xyz.jhmapstruct.service.criteria.ReviewGammaCriteria;

/**
 * Service for executing complex queries for {@link ReviewGamma} entities in the database.
 * The main input is a {@link ReviewGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewGammaQueryService extends QueryService<ReviewGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewGammaQueryService.class);

    private final ReviewGammaRepository reviewGammaRepository;

    public ReviewGammaQueryService(ReviewGammaRepository reviewGammaRepository) {
        this.reviewGammaRepository = reviewGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link ReviewGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewGamma> findByCriteria(ReviewGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewGamma> specification = createSpecification(criteria);
        return reviewGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewGamma> specification = createSpecification(criteria);
        return reviewGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewGamma> createSpecification(ReviewGammaCriteria criteria) {
        Specification<ReviewGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewGamma_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewGamma_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewGamma_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewGamma_.product, JoinType.LEFT).get(ProductGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
