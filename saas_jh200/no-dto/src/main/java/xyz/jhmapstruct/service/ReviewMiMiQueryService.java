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
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.criteria.ReviewMiMiCriteria;

/**
 * Service for executing complex queries for {@link ReviewMiMi} entities in the database.
 * The main input is a {@link ReviewMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewMiMiQueryService extends QueryService<ReviewMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiMiQueryService.class);

    private final ReviewMiMiRepository reviewMiMiRepository;

    public ReviewMiMiQueryService(ReviewMiMiRepository reviewMiMiRepository) {
        this.reviewMiMiRepository = reviewMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link ReviewMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewMiMi> findByCriteria(ReviewMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewMiMi> specification = createSpecification(criteria);
        return reviewMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewMiMi> specification = createSpecification(criteria);
        return reviewMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewMiMi> createSpecification(ReviewMiMiCriteria criteria) {
        Specification<ReviewMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewMiMi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewMiMi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewMiMi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewMiMi_.product, JoinType.LEFT).get(ProductMiMi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
