package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlDesire;
import ai.realworld.repository.AlDesireRepository;
import ai.realworld.service.criteria.AlDesireCriteria;
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
 * Service for executing complex queries for {@link AlDesire} entities in the database.
 * The main input is a {@link AlDesireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlDesire} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlDesireQueryService extends QueryService<AlDesire> {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireQueryService.class);

    private final AlDesireRepository alDesireRepository;

    public AlDesireQueryService(AlDesireRepository alDesireRepository) {
        this.alDesireRepository = alDesireRepository;
    }

    /**
     * Return a {@link Page} of {@link AlDesire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlDesire> findByCriteria(AlDesireCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlDesire> specification = createSpecification(criteria);
        return alDesireRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlDesireCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlDesire> specification = createSpecification(criteria);
        return alDesireRepository.count(specification);
    }

    /**
     * Function to convert {@link AlDesireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlDesire> createSpecification(AlDesireCriteria criteria) {
        Specification<AlDesire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlDesire_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlDesire_.name));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlDesire_.weight));
            }
            if (criteria.getProbabilityOfWinning() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getProbabilityOfWinning(), AlDesire_.probabilityOfWinning)
                );
            }
            if (criteria.getMaximumWinningTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximumWinningTime(), AlDesire_.maximumWinningTime));
            }
            if (criteria.getIsWinningTimeLimited() != null) {
                specification = specification.and(buildSpecification(criteria.getIsWinningTimeLimited(), AlDesire_.isWinningTimeLimited));
            }
            if (criteria.getAwardResultType() != null) {
                specification = specification.and(buildSpecification(criteria.getAwardResultType(), AlDesire_.awardResultType));
            }
            if (criteria.getAwardReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAwardReference(), AlDesire_.awardReference));
            }
            if (criteria.getIsDefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefault(), AlDesire_.isDefault));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlDesire_.image, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getMaggiId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaggiId(), root -> root.join(AlDesire_.maggi, JoinType.LEFT).get(AlLeandro_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlDesire_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
