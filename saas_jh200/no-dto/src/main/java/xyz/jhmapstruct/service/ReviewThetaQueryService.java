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
import xyz.jhmapstruct.domain.ReviewTheta;
import xyz.jhmapstruct.repository.ReviewThetaRepository;
import xyz.jhmapstruct.service.criteria.ReviewThetaCriteria;

/**
 * Service for executing complex queries for {@link ReviewTheta} entities in the database.
 * The main input is a {@link ReviewThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewThetaQueryService extends QueryService<ReviewTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewThetaQueryService.class);

    private final ReviewThetaRepository reviewThetaRepository;

    public ReviewThetaQueryService(ReviewThetaRepository reviewThetaRepository) {
        this.reviewThetaRepository = reviewThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link ReviewTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewTheta> findByCriteria(ReviewThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewTheta> specification = createSpecification(criteria);
        return reviewThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewTheta> specification = createSpecification(criteria);
        return reviewThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewTheta> createSpecification(ReviewThetaCriteria criteria) {
        Specification<ReviewTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewTheta_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewTheta_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewTheta_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewTheta_.product, JoinType.LEFT).get(ProductTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
