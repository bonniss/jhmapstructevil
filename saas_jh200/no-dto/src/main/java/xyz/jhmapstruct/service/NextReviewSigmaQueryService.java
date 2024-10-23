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
import xyz.jhmapstruct.domain.NextReviewSigma;
import xyz.jhmapstruct.repository.NextReviewSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextReviewSigmaCriteria;

/**
 * Service for executing complex queries for {@link NextReviewSigma} entities in the database.
 * The main input is a {@link NextReviewSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewSigma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewSigmaQueryService extends QueryService<NextReviewSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewSigmaQueryService.class);

    private final NextReviewSigmaRepository nextReviewSigmaRepository;

    public NextReviewSigmaQueryService(NextReviewSigmaRepository nextReviewSigmaRepository) {
        this.nextReviewSigmaRepository = nextReviewSigmaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextReviewSigma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewSigma> findByCriteria(NextReviewSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewSigma> specification = createSpecification(criteria);
        return nextReviewSigmaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewSigma> specification = createSpecification(criteria);
        return nextReviewSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewSigma> createSpecification(NextReviewSigmaCriteria criteria) {
        Specification<NextReviewSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewSigma_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewSigma_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewSigma_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewSigma_.product, JoinType.LEFT).get(NextProductSigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
