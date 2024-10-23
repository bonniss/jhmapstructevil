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
import xyz.jhmapstruct.domain.NextReviewMi;
import xyz.jhmapstruct.repository.NextReviewMiRepository;
import xyz.jhmapstruct.service.criteria.NextReviewMiCriteria;

/**
 * Service for executing complex queries for {@link NextReviewMi} entities in the database.
 * The main input is a {@link NextReviewMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewMiQueryService extends QueryService<NextReviewMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiQueryService.class);

    private final NextReviewMiRepository nextReviewMiRepository;

    public NextReviewMiQueryService(NextReviewMiRepository nextReviewMiRepository) {
        this.nextReviewMiRepository = nextReviewMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextReviewMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewMi> findByCriteria(NextReviewMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewMi> specification = createSpecification(criteria);
        return nextReviewMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewMi> specification = createSpecification(criteria);
        return nextReviewMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewMi> createSpecification(NextReviewMiCriteria criteria) {
        Specification<NextReviewMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewMi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewMi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewMi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(NextReviewMi_.product, JoinType.LEFT).get(ProductMi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextReviewMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
