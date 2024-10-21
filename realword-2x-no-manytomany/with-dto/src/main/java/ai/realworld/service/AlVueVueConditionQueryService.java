package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlVueVueCondition;
import ai.realworld.repository.AlVueVueConditionRepository;
import ai.realworld.service.criteria.AlVueVueConditionCriteria;
import ai.realworld.service.dto.AlVueVueConditionDTO;
import ai.realworld.service.mapper.AlVueVueConditionMapper;
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
 * Service for executing complex queries for {@link AlVueVueCondition} entities in the database.
 * The main input is a {@link AlVueVueConditionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlVueVueConditionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlVueVueConditionQueryService extends QueryService<AlVueVueCondition> {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueConditionQueryService.class);

    private final AlVueVueConditionRepository alVueVueConditionRepository;

    private final AlVueVueConditionMapper alVueVueConditionMapper;

    public AlVueVueConditionQueryService(
        AlVueVueConditionRepository alVueVueConditionRepository,
        AlVueVueConditionMapper alVueVueConditionMapper
    ) {
        this.alVueVueConditionRepository = alVueVueConditionRepository;
        this.alVueVueConditionMapper = alVueVueConditionMapper;
    }

    /**
     * Return a {@link Page} of {@link AlVueVueConditionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlVueVueConditionDTO> findByCriteria(AlVueVueConditionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlVueVueCondition> specification = createSpecification(criteria);
        return alVueVueConditionRepository.findAll(specification, page).map(alVueVueConditionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlVueVueConditionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlVueVueCondition> specification = createSpecification(criteria);
        return alVueVueConditionRepository.count(specification);
    }

    /**
     * Function to convert {@link AlVueVueConditionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlVueVueCondition> createSpecification(AlVueVueConditionCriteria criteria) {
        Specification<AlVueVueCondition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlVueVueCondition_.id));
            }
            if (criteria.getSubjectType() != null) {
                specification = specification.and(buildSpecification(criteria.getSubjectType(), AlVueVueCondition_.subjectType));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubject(), AlVueVueCondition_.subject));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), AlVueVueCondition_.action));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), AlVueVueCondition_.note));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlVueVueCondition_.parent, JoinType.LEFT).get(AlVueVue_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlVueVueCondition_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
