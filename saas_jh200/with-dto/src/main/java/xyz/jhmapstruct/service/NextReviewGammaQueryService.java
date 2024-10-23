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
import xyz.jhmapstruct.domain.NextReviewGamma;
import xyz.jhmapstruct.repository.NextReviewGammaRepository;
import xyz.jhmapstruct.service.criteria.NextReviewGammaCriteria;
import xyz.jhmapstruct.service.dto.NextReviewGammaDTO;
import xyz.jhmapstruct.service.mapper.NextReviewGammaMapper;

/**
 * Service for executing complex queries for {@link NextReviewGamma} entities in the database.
 * The main input is a {@link NextReviewGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextReviewGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextReviewGammaQueryService extends QueryService<NextReviewGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewGammaQueryService.class);

    private final NextReviewGammaRepository nextReviewGammaRepository;

    private final NextReviewGammaMapper nextReviewGammaMapper;

    public NextReviewGammaQueryService(NextReviewGammaRepository nextReviewGammaRepository, NextReviewGammaMapper nextReviewGammaMapper) {
        this.nextReviewGammaRepository = nextReviewGammaRepository;
        this.nextReviewGammaMapper = nextReviewGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextReviewGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextReviewGammaDTO> findByCriteria(NextReviewGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextReviewGamma> specification = createSpecification(criteria);
        return nextReviewGammaRepository.findAll(specification, page).map(nextReviewGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextReviewGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextReviewGamma> specification = createSpecification(criteria);
        return nextReviewGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextReviewGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextReviewGamma> createSpecification(NextReviewGammaCriteria criteria) {
        Specification<NextReviewGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextReviewGamma_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), NextReviewGamma_.rating));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), NextReviewGamma_.reviewDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(NextReviewGamma_.product, JoinType.LEFT).get(NextProductGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextReviewGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
