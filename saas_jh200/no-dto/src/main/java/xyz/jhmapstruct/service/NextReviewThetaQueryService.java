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
import xyz.jhmapstruct.domain.NextReviewTheta;
import xyz.jhmapstruct.repository.NextReviewThetaRepository;
import xyz.jhmapstruct.service.criteria.NextReviewThetaCriteria;

/**
 * Service for executing complex queries for {@link NextReviewTheta} entities in the database.
 * The main input is a {@link NextReviewThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewThetaQueryService extends QueryService<NextReviewTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewThetaQueryService.class);

    private final NextReviewThetaRepository nextReviewThetaRepository;

    public NextReviewThetaQueryService(NextReviewThetaRepository nextReviewThetaRepository) {
        this.nextReviewThetaRepository = nextReviewThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextReviewTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewTheta> findByCriteria(NextReviewThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewTheta> specification = createSpecification(criteria);
        return nextReviewThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewTheta> specification = createSpecification(criteria);
        return nextReviewThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewTheta> createSpecification(NextReviewThetaCriteria criteria) {
        Specification<NextReviewTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewTheta_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewTheta_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewTheta_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewTheta_.product, JoinType.LEFT).get(NextProductTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
