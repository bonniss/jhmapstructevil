package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlGoreCondition;
import ai.realworld.repository.AlGoreConditionRepository;
import ai.realworld.service.criteria.AlGoreConditionCriteria;
import ai.realworld.service.dto.AlGoreConditionDTO;
import ai.realworld.service.mapper.AlGoreConditionMapper;
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
 * Service for executing complex queries for {@link AlGoreCondition} entities in the database.
 * The main input is a {@link AlGoreConditionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlGoreConditionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlGoreConditionQueryService extends QueryService<AlGoreCondition> {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionQueryService.class);

    private final AlGoreConditionRepository alGoreConditionRepository;

    private final AlGoreConditionMapper alGoreConditionMapper;

    public AlGoreConditionQueryService(AlGoreConditionRepository alGoreConditionRepository, AlGoreConditionMapper alGoreConditionMapper) {
        this.alGoreConditionRepository = alGoreConditionRepository;
        this.alGoreConditionMapper = alGoreConditionMapper;
    }

    /**
     * Return a {@link Page} of {@link AlGoreConditionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlGoreConditionDTO> findByCriteria(AlGoreConditionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlGoreCondition> specification = createSpecification(criteria);
        return alGoreConditionRepository.findAll(specification, page).map(alGoreConditionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlGoreConditionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlGoreCondition> specification = createSpecification(criteria);
        return alGoreConditionRepository.count(specification);
    }

    /**
     * Function to convert {@link AlGoreConditionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlGoreCondition> createSpecification(AlGoreConditionCriteria criteria) {
        Specification<AlGoreCondition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlGoreCondition_.id));
            }
            if (criteria.getSubjectType() != null) {
                specification = specification.and(buildSpecification(criteria.getSubjectType(), AlGoreCondition_.subjectType));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubject(), AlGoreCondition_.subject));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), AlGoreCondition_.action));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), AlGoreCondition_.note));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlGoreCondition_.parent, JoinType.LEFT).get(AlGore_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlGoreCondition_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
