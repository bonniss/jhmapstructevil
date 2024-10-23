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
import xyz.jhmapstruct.domain.NextReviewBeta;
import xyz.jhmapstruct.repository.NextReviewBetaRepository;
import xyz.jhmapstruct.service.criteria.NextReviewBetaCriteria;

/**
 * Service for executing complex queries for {@link NextReviewBeta} entities in the database.
 * The main input is a {@link NextReviewBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewBetaQueryService extends QueryService<NextReviewBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewBetaQueryService.class);

    private final NextReviewBetaRepository nextReviewBetaRepository;

    public NextReviewBetaQueryService(NextReviewBetaRepository nextReviewBetaRepository) {
        this.nextReviewBetaRepository = nextReviewBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextReviewBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewBeta> findByCriteria(NextReviewBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewBeta> specification = createSpecification(criteria);
        return nextReviewBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewBeta> specification = createSpecification(criteria);
        return nextReviewBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewBeta> createSpecification(NextReviewBetaCriteria criteria) {
        Specification<NextReviewBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewBeta_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewBeta_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewBeta_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewBeta_.product, JoinType.LEFT).get(NextProductBeta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
