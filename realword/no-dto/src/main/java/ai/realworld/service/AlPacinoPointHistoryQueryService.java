package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacinoPointHistory;
import ai.realworld.repository.AlPacinoPointHistoryRepository;
import ai.realworld.service.criteria.AlPacinoPointHistoryCriteria;
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
 * Service for executing complex queries for {@link AlPacinoPointHistory} entities in the database.
 * The main input is a {@link AlPacinoPointHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoPointHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoPointHistoryQueryService extends QueryService<AlPacinoPointHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryQueryService.class);

    private final AlPacinoPointHistoryRepository alPacinoPointHistoryRepository;

    public AlPacinoPointHistoryQueryService(AlPacinoPointHistoryRepository alPacinoPointHistoryRepository) {
        this.alPacinoPointHistoryRepository = alPacinoPointHistoryRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoPointHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoPointHistory> findByCriteria(AlPacinoPointHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacinoPointHistory> specification = createSpecification(criteria);
        return alPacinoPointHistoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoPointHistoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacinoPointHistory> specification = createSpecification(criteria);
        return alPacinoPointHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoPointHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacinoPointHistory> createSpecification(AlPacinoPointHistoryCriteria criteria) {
        Specification<AlPacinoPointHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPacinoPointHistory_.id));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildSpecification(criteria.getSource(), AlPacinoPointHistory_.source));
            }
            if (criteria.getAssociatedId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssociatedId(), AlPacinoPointHistory_.associatedId));
            }
            if (criteria.getPointAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointAmount(), AlPacinoPointHistory_.pointAmount));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlPacinoPointHistory_.customer, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPacinoPointHistory_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
