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
import xyz.jhmapstruct.domain.NextReviewMiMi;
import xyz.jhmapstruct.repository.NextReviewMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextReviewMiMiCriteria;

/**
 * Service for executing complex queries for {@link NextReviewMiMi} entities in the database.
 * The main input is a {@link NextReviewMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewMiMiQueryService extends QueryService<NextReviewMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiMiQueryService.class);

    private final NextReviewMiMiRepository nextReviewMiMiRepository;

    public NextReviewMiMiQueryService(NextReviewMiMiRepository nextReviewMiMiRepository) {
        this.nextReviewMiMiRepository = nextReviewMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextReviewMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewMiMi> findByCriteria(NextReviewMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewMiMi> specification = createSpecification(criteria);
        return nextReviewMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewMiMi> specification = createSpecification(criteria);
        return nextReviewMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewMiMi> createSpecification(NextReviewMiMiCriteria criteria) {
        Specification<NextReviewMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewMiMi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewMiMi_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewMiMi_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewMiMi_.product, JoinType.LEFT).get(NextProductMiMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
