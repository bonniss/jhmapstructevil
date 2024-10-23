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
import xyz.jhmapstruct.domain.ReviewAlpha;
import xyz.jhmapstruct.repository.ReviewAlphaRepository;
import xyz.jhmapstruct.service.criteria.ReviewAlphaCriteria;
import xyz.jhmapstruct.service.dto.ReviewAlphaDTO;
import xyz.jhmapstruct.service.mapper.ReviewAlphaMapper;

/**
 * Service for executing complex queries for {@link ReviewAlpha} entities in the database.
 * The main input is a {@link ReviewAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewAlphaQueryService extends QueryService<ReviewAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewAlphaQueryService.class);

    private final ReviewAlphaRepository reviewAlphaRepository;

    private final ReviewAlphaMapper reviewAlphaMapper;

    public ReviewAlphaQueryService(ReviewAlphaRepository reviewAlphaRepository, ReviewAlphaMapper reviewAlphaMapper) {
        this.reviewAlphaRepository = reviewAlphaRepository;
        this.reviewAlphaMapper = reviewAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link ReviewAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewAlphaDTO> findByCriteria(ReviewAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewAlpha> specification = createSpecification(criteria);
        return reviewAlphaRepository.findAll(specification, page).map(reviewAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewAlpha> specification = createSpecification(criteria);
        return reviewAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewAlpha> createSpecification(ReviewAlphaCriteria criteria) {
        Specification<ReviewAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewAlpha_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewAlpha_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewAlpha_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewAlpha_.product, JoinType.LEFT).get(ProductAlpha_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
