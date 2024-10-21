package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLeandroPlayingTime;
import ai.realworld.repository.AlLeandroPlayingTimeRepository;
import ai.realworld.service.criteria.AlLeandroPlayingTimeCriteria;
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
 * Service for executing complex queries for {@link AlLeandroPlayingTime} entities in the database.
 * The main input is a {@link AlLeandroPlayingTimeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLeandroPlayingTime} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLeandroPlayingTimeQueryService extends QueryService<AlLeandroPlayingTime> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeQueryService.class);

    private final AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository;

    public AlLeandroPlayingTimeQueryService(AlLeandroPlayingTimeRepository alLeandroPlayingTimeRepository) {
        this.alLeandroPlayingTimeRepository = alLeandroPlayingTimeRepository;
    }

    /**
     * Return a {@link Page} of {@link AlLeandroPlayingTime} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLeandroPlayingTime> findByCriteria(AlLeandroPlayingTimeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLeandroPlayingTime> specification = createSpecification(criteria);
        return alLeandroPlayingTimeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLeandroPlayingTimeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLeandroPlayingTime> specification = createSpecification(criteria);
        return alLeandroPlayingTimeRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLeandroPlayingTimeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLeandroPlayingTime> createSpecification(AlLeandroPlayingTimeCriteria criteria) {
        Specification<AlLeandroPlayingTime> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlLeandroPlayingTime_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AlLeandroPlayingTime_.status));
            }
            if (criteria.getWonDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWonDate(), AlLeandroPlayingTime_.wonDate));
            }
            if (criteria.getSentAwardToPlayerAt() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getSentAwardToPlayerAt(), AlLeandroPlayingTime_.sentAwardToPlayerAt)
                );
            }
            if (criteria.getSentAwardToPlayerBy() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getSentAwardToPlayerBy(), AlLeandroPlayingTime_.sentAwardToPlayerBy)
                );
            }
            if (criteria.getPlayerReceivedTheAwardAt() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getPlayerReceivedTheAwardAt(), AlLeandroPlayingTime_.playerReceivedTheAwardAt)
                );
            }
            if (criteria.getPlaySourceTime() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPlaySourceTime(), AlLeandroPlayingTime_.playSourceTime)
                );
            }
            if (criteria.getMaggiId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaggiId(), root ->
                        root.join(AlLeandroPlayingTime_.maggi, JoinType.LEFT).get(AlLeandro_.id)
                    )
                );
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(AlLeandroPlayingTime_.user, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getAwardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAwardId(), root ->
                        root.join(AlLeandroPlayingTime_.award, JoinType.LEFT).get(AlDesire_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlLeandroPlayingTime_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
