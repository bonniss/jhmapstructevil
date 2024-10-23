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
import xyz.jhmapstruct.domain.ReviewBeta;
import xyz.jhmapstruct.repository.ReviewBetaRepository;
import xyz.jhmapstruct.service.criteria.ReviewBetaCriteria;
import xyz.jhmapstruct.service.dto.ReviewBetaDTO;
import xyz.jhmapstruct.service.mapper.ReviewBetaMapper;

/**
 * Service for executing complex queries for {@link ReviewBeta} entities in the database.
 * The main input is a {@link ReviewBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewBetaQueryService extends QueryService<ReviewBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewBetaQueryService.class);

    private final ReviewBetaRepository reviewBetaRepository;

    private final ReviewBetaMapper reviewBetaMapper;

    public ReviewBetaQueryService(ReviewBetaRepository reviewBetaRepository, ReviewBetaMapper reviewBetaMapper) {
        this.reviewBetaRepository = reviewBetaRepository;
        this.reviewBetaMapper = reviewBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link ReviewBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewBetaDTO> findByCriteria(ReviewBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewBeta> specification = createSpecification(criteria);
        return reviewBetaRepository.findAll(specification, page).map(reviewBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewBeta> specification = createSpecification(criteria);
        return reviewBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewBeta> createSpecification(ReviewBetaCriteria criteria) {
        Specification<ReviewBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewBeta_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewBeta_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewBeta_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewBeta_.product, JoinType.LEFT).get(ProductBeta_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}