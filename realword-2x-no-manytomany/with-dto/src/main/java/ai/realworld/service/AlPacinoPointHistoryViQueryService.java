package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacinoPointHistoryVi;
import ai.realworld.repository.AlPacinoPointHistoryViRepository;
import ai.realworld.service.criteria.AlPacinoPointHistoryViCriteria;
import ai.realworld.service.dto.AlPacinoPointHistoryViDTO;
import ai.realworld.service.mapper.AlPacinoPointHistoryViMapper;
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
 * Service for executing complex queries for {@link AlPacinoPointHistoryVi} entities in the database.
 * The main input is a {@link AlPacinoPointHistoryViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoPointHistoryViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoPointHistoryViQueryService extends QueryService<AlPacinoPointHistoryVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryViQueryService.class);

    private final AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository;

    private final AlPacinoPointHistoryViMapper alPacinoPointHistoryViMapper;

    public AlPacinoPointHistoryViQueryService(
        AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository,
        AlPacinoPointHistoryViMapper alPacinoPointHistoryViMapper
    ) {
        this.alPacinoPointHistoryViRepository = alPacinoPointHistoryViRepository;
        this.alPacinoPointHistoryViMapper = alPacinoPointHistoryViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoPointHistoryViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoPointHistoryViDTO> findByCriteria(AlPacinoPointHistoryViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacinoPointHistoryVi> specification = createSpecification(criteria);
        return alPacinoPointHistoryViRepository.findAll(specification, page).map(alPacinoPointHistoryViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoPointHistoryViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacinoPointHistoryVi> specification = createSpecification(criteria);
        return alPacinoPointHistoryViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoPointHistoryViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacinoPointHistoryVi> createSpecification(AlPacinoPointHistoryViCriteria criteria) {
        Specification<AlPacinoPointHistoryVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPacinoPointHistoryVi_.id));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildSpecification(criteria.getSource(), AlPacinoPointHistoryVi_.source));
            }
            if (criteria.getAssociatedId() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getAssociatedId(), AlPacinoPointHistoryVi_.associatedId)
                );
            }
            if (criteria.getPointAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPointAmount(), AlPacinoPointHistoryVi_.pointAmount));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlPacinoPointHistoryVi_.customer, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPacinoPointHistoryVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
