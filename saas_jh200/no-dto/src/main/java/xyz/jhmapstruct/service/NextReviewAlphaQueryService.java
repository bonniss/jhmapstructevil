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
import xyz.jhmapstruct.domain.NextReviewAlpha;
import xyz.jhmapstruct.repository.NextReviewAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextReviewAlphaCriteria;

/**
 * Service for executing complex queries for {@link NextReviewAlpha} entities in the database.
 * The main input is a {@link NextReviewAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewAlpha} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewAlphaQueryService extends QueryService<NextReviewAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewAlphaQueryService.class);

    private final NextReviewAlphaRepository nextReviewAlphaRepository;

    public NextReviewAlphaQueryService(NextReviewAlphaRepository nextReviewAlphaRepository) {
        this.nextReviewAlphaRepository = nextReviewAlphaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextReviewAlpha} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewAlpha> findByCriteria(NextReviewAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewAlpha> specification = createSpecification(criteria);
        return nextReviewAlphaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewAlpha> specification = createSpecification(criteria);
        return nextReviewAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewAlpha> createSpecification(NextReviewAlphaCriteria criteria) {
        Specification<NextReviewAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewAlpha_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewAlpha_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewAlpha_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewAlpha_.product, JoinType.LEFT).get(NextProductAlpha_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
