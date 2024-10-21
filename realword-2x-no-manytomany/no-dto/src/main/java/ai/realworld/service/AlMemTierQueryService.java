package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlMemTier;
import ai.realworld.repository.AlMemTierRepository;
import ai.realworld.service.criteria.AlMemTierCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlMemTier} entities in the database.
 * The main input is a {@link AlMemTierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlMemTier} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlMemTierQueryService extends QueryService<AlMemTier> {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierQueryService.class);

    private final AlMemTierRepository alMemTierRepository;

    public AlMemTierQueryService(AlMemTierRepository alMemTierRepository) {
        this.alMemTierRepository = alMemTierRepository;
    }

    /**
     * Return a {@link Page} of {@link AlMemTier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlMemTier> findByCriteria(AlMemTierCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlMemTier> specification = createSpecification(criteria);
        return alMemTierRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlMemTierCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlMemTier> specification = createSpecification(criteria);
        return alMemTierRepository.count(specification);
    }

    /**
     * Function to convert {@link AlMemTierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlMemTier> createSpecification(AlMemTierCriteria criteria) {
        Specification<AlMemTier> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlMemTier_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlMemTier_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlMemTier_.description));
            }
            if (criteria.getMinPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinPoint(), AlMemTier_.minPoint));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlMemTier_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
