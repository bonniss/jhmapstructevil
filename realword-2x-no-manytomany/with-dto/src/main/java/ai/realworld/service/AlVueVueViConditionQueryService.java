package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlVueVueViCondition;
import ai.realworld.repository.AlVueVueViConditionRepository;
import ai.realworld.service.criteria.AlVueVueViConditionCriteria;
import ai.realworld.service.dto.AlVueVueViConditionDTO;
import ai.realworld.service.mapper.AlVueVueViConditionMapper;
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
 * Service for executing complex queries for {@link AlVueVueViCondition} entities in the database.
 * The main input is a {@link AlVueVueViConditionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlVueVueViConditionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlVueVueViConditionQueryService extends QueryService<AlVueVueViCondition> {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViConditionQueryService.class);

    private final AlVueVueViConditionRepository alVueVueViConditionRepository;

    private final AlVueVueViConditionMapper alVueVueViConditionMapper;

    public AlVueVueViConditionQueryService(
        AlVueVueViConditionRepository alVueVueViConditionRepository,
        AlVueVueViConditionMapper alVueVueViConditionMapper
    ) {
        this.alVueVueViConditionRepository = alVueVueViConditionRepository;
        this.alVueVueViConditionMapper = alVueVueViConditionMapper;
    }

    /**
     * Return a {@link Page} of {@link AlVueVueViConditionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlVueVueViConditionDTO> findByCriteria(AlVueVueViConditionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlVueVueViCondition> specification = createSpecification(criteria);
        return alVueVueViConditionRepository.findAll(specification, page).map(alVueVueViConditionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlVueVueViConditionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlVueVueViCondition> specification = createSpecification(criteria);
        return alVueVueViConditionRepository.count(specification);
    }

    /**
     * Function to convert {@link AlVueVueViConditionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlVueVueViCondition> createSpecification(AlVueVueViConditionCriteria criteria) {
        Specification<AlVueVueViCondition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlVueVueViCondition_.id));
            }
            if (criteria.getSubjectType() != null) {
                specification = specification.and(buildSpecification(criteria.getSubjectType(), AlVueVueViCondition_.subjectType));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubject(), AlVueVueViCondition_.subject));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), AlVueVueViCondition_.action));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), AlVueVueViCondition_.note));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root ->
                        root.join(AlVueVueViCondition_.parent, JoinType.LEFT).get(AlVueVueVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlVueVueViCondition_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
