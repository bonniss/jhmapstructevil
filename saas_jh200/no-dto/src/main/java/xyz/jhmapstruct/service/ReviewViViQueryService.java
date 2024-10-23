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
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.repository.ReviewViViRepository;
import xyz.jhmapstruct.service.criteria.ReviewViViCriteria;

/**
 * Service for executing complex queries for {@link ReviewViVi} entities in the database.
 * The main input is a {@link ReviewViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewViViQueryService extends QueryService<ReviewViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViViQueryService.class);

    private final ReviewViViRepository reviewViViRepository;

    public ReviewViViQueryService(ReviewViViRepository reviewViViRepository) {
        this.reviewViViRepository = reviewViViRepository;
    }

    /**
     * Return a {@link Page} of {@link ReviewViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewViVi> findByCriteria(ReviewViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewViVi> specification = createSpecification(criteria);
        return reviewViViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewViVi> specification = createSpecification(criteria);
        return reviewViViRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewViVi> createSpecification(ReviewViViCriteria criteria) {
        Specification<ReviewViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewViVi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewViVi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewViVi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewViVi_.product, JoinType.LEFT).get(ProductViVi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
