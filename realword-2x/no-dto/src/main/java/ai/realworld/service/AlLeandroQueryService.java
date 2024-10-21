package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLeandro;
import ai.realworld.repository.AlLeandroRepository;
import ai.realworld.service.criteria.AlLeandroCriteria;
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
 * Service for executing complex queries for {@link AlLeandro} entities in the database.
 * The main input is a {@link AlLeandroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLeandro} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLeandroQueryService extends QueryService<AlLeandro> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroQueryService.class);

    private final AlLeandroRepository alLeandroRepository;

    public AlLeandroQueryService(AlLeandroRepository alLeandroRepository) {
        this.alLeandroRepository = alLeandroRepository;
    }

    /**
     * Return a {@link Page} of {@link AlLeandro} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLeandro> findByCriteria(AlLeandroCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLeandro> specification = createSpecification(criteria);
        return alLeandroRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLeandroCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLeandro> specification = createSpecification(criteria);
        return alLeandroRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLeandroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLeandro> createSpecification(AlLeandroCriteria criteria) {
        Specification<AlLeandro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlLeandro_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlLeandro_.name));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlLeandro_.weight));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlLeandro_.description));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), AlLeandro_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), AlLeandro_.toDate));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlLeandro_.isEnabled));
            }
            if (criteria.getSeparateWinningByPeriods() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSeparateWinningByPeriods(), AlLeandro_.separateWinningByPeriods)
                );
            }
            if (criteria.getProgramBackgroundId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProgramBackgroundId(), root ->
                        root.join(AlLeandro_.programBackground, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getWheelBackgroundId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getWheelBackgroundId(), root ->
                        root.join(AlLeandro_.wheelBackground, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlLeandro_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAwardsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAwardsId(), root -> root.join(AlLeandro_.awards, JoinType.LEFT).get(AlDesire_.id))
                );
            }
            if (criteria.getAwardVisId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAwardVisId(), root -> root.join(AlLeandro_.awardVis, JoinType.LEFT).get(AlDesireVi_.id))
                );
            }
        }
        return specification;
    }
}
