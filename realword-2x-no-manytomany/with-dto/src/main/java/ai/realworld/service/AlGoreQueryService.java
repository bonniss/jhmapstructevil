package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlGore;
import ai.realworld.repository.AlGoreRepository;
import ai.realworld.service.criteria.AlGoreCriteria;
import ai.realworld.service.dto.AlGoreDTO;
import ai.realworld.service.mapper.AlGoreMapper;
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
 * Service for executing complex queries for {@link AlGore} entities in the database.
 * The main input is a {@link AlGoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlGoreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlGoreQueryService extends QueryService<AlGore> {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreQueryService.class);

    private final AlGoreRepository alGoreRepository;

    private final AlGoreMapper alGoreMapper;

    public AlGoreQueryService(AlGoreRepository alGoreRepository, AlGoreMapper alGoreMapper) {
        this.alGoreRepository = alGoreRepository;
        this.alGoreMapper = alGoreMapper;
    }

    /**
     * Return a {@link Page} of {@link AlGoreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlGoreDTO> findByCriteria(AlGoreCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlGore> specification = createSpecification(criteria);
        return alGoreRepository.findAll(specification, page).map(alGoreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlGoreCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlGore> specification = createSpecification(criteria);
        return alGoreRepository.count(specification);
    }

    /**
     * Function to convert {@link AlGoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlGore> createSpecification(AlGoreCriteria criteria) {
        Specification<AlGore> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlGore_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlGore_.name));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), AlGore_.discountType));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), AlGore_.discountRate));
            }
            if (criteria.getScope() != null) {
                specification = specification.and(buildSpecification(criteria.getScope(), AlGore_.scope));
            }
            if (criteria.getBizRelationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBizRelationId(), root ->
                        root.join(AlGore_.bizRelation, JoinType.LEFT).get(AlBetonamuRelation_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlGore_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getBizRelationViId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBizRelationViId(), root ->
                        root.join(AlGore_.bizRelationVi, JoinType.LEFT).get(AlBetonamuRelationVi_.id)
                    )
                );
            }
            if (criteria.getConditionsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getConditionsId(), root ->
                        root.join(AlGore_.conditions, JoinType.LEFT).get(AlGoreCondition_.id)
                    )
                );
            }
            if (criteria.getConditionVisId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getConditionVisId(), root ->
                        root.join(AlGore_.conditionVis, JoinType.LEFT).get(AlGoreConditionVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
