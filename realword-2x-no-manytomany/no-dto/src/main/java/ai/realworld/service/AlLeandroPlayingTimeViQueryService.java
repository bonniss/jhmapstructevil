package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLeandroPlayingTimeVi;
import ai.realworld.repository.AlLeandroPlayingTimeViRepository;
import ai.realworld.service.criteria.AlLeandroPlayingTimeViCriteria;
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
 * Service for executing complex queries for {@link AlLeandroPlayingTimeVi} entities in the database.
 * The main input is a {@link AlLeandroPlayingTimeViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLeandroPlayingTimeVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLeandroPlayingTimeViQueryService extends QueryService<AlLeandroPlayingTimeVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeViQueryService.class);

    private final AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository;

    public AlLeandroPlayingTimeViQueryService(AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository) {
        this.alLeandroPlayingTimeViRepository = alLeandroPlayingTimeViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlLeandroPlayingTimeVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLeandroPlayingTimeVi> findByCriteria(AlLeandroPlayingTimeViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLeandroPlayingTimeVi> specification = createSpecification(criteria);
        return alLeandroPlayingTimeViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLeandroPlayingTimeViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLeandroPlayingTimeVi> specification = createSpecification(criteria);
        return alLeandroPlayingTimeViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLeandroPlayingTimeViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLeandroPlayingTimeVi> createSpecification(AlLeandroPlayingTimeViCriteria criteria) {
        Specification<AlLeandroPlayingTimeVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlLeandroPlayingTimeVi_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AlLeandroPlayingTimeVi_.status));
            }
            if (criteria.getWonDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWonDate(), AlLeandroPlayingTimeVi_.wonDate));
            }
            if (criteria.getSentAwardToPlayerAt() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getSentAwardToPlayerAt(), AlLeandroPlayingTimeVi_.sentAwardToPlayerAt)
                );
            }
            if (criteria.getSentAwardToPlayerBy() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getSentAwardToPlayerBy(), AlLeandroPlayingTimeVi_.sentAwardToPlayerBy)
                );
            }
            if (criteria.getPlayerReceivedTheAwardAt() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getPlayerReceivedTheAwardAt(), AlLeandroPlayingTimeVi_.playerReceivedTheAwardAt)
                );
            }
            if (criteria.getPlaySourceTime() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPlaySourceTime(), AlLeandroPlayingTimeVi_.playSourceTime)
                );
            }
            if (criteria.getMaggiId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaggiId(), root ->
                        root.join(AlLeandroPlayingTimeVi_.maggi, JoinType.LEFT).get(AlLeandro_.id)
                    )
                );
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root ->
                        root.join(AlLeandroPlayingTimeVi_.user, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
            if (criteria.getAwardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAwardId(), root ->
                        root.join(AlLeandroPlayingTimeVi_.award, JoinType.LEFT).get(AlDesire_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlLeandroPlayingTimeVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
