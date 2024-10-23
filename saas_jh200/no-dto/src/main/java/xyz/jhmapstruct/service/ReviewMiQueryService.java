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
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.criteria.ReviewMiCriteria;

/**
 * Service for executing complex queries for {@link ReviewMi} entities in the database.
 * The main input is a {@link ReviewMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewMiQueryService extends QueryService<ReviewMi> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiQueryService.class);

    private final ReviewMiRepository reviewMiRepository;

    public ReviewMiQueryService(ReviewMiRepository reviewMiRepository) {
        this.reviewMiRepository = reviewMiRepository;
    }

    /**
     * Return a {@link Page} of {@link ReviewMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewMi> findByCriteria(ReviewMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewMi> specification = createSpecification(criteria);
        return reviewMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewMi> specification = createSpecification(criteria);
        return reviewMiRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewMi> createSpecification(ReviewMiCriteria criteria) {
        Specification<ReviewMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewMi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewMi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewMi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewMi_.product, JoinType.LEFT).get(NextProductMi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
