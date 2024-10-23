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
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.repository.ReviewViRepository;
import xyz.jhmapstruct.service.criteria.ReviewViCriteria;
import xyz.jhmapstruct.service.dto.ReviewViDTO;
import xyz.jhmapstruct.service.mapper.ReviewViMapper;

/**
 * Service for executing complex queries for {@link ReviewVi} entities in the database.
 * The main input is a {@link ReviewViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReviewViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewViQueryService extends QueryService<ReviewVi> {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViQueryService.class);

    private final ReviewViRepository reviewViRepository;

    private final ReviewViMapper reviewViMapper;

    public ReviewViQueryService(ReviewViRepository reviewViRepository, ReviewViMapper reviewViMapper) {
        this.reviewViRepository = reviewViRepository;
        this.reviewViMapper = reviewViMapper;
    }

    /**
     * Return a {@link Page} of {@link ReviewViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewViDTO> findByCriteria(ReviewViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReviewVi> specification = createSpecification(criteria);
        return reviewViRepository.findAll(specification, page).map(reviewViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReviewVi> specification = createSpecification(criteria);
        return reviewViRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReviewVi> createSpecification(ReviewViCriteria criteria) {
        Specification<ReviewVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReviewVi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), ReviewVi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), ReviewVi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(ReviewVi_.product, JoinType.LEFT).get(ProductVi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ReviewVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
