package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlGoreConditionVi;
import ai.realworld.repository.AlGoreConditionViRepository;
import ai.realworld.service.criteria.AlGoreConditionViCriteria;
import ai.realworld.service.dto.AlGoreConditionViDTO;
import ai.realworld.service.mapper.AlGoreConditionViMapper;
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
 * Service for executing complex queries for {@link AlGoreConditionVi} entities in the database.
 * The main input is a {@link AlGoreConditionViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlGoreConditionViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlGoreConditionViQueryService extends QueryService<AlGoreConditionVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionViQueryService.class);

    private final AlGoreConditionViRepository alGoreConditionViRepository;

    private final AlGoreConditionViMapper alGoreConditionViMapper;

    public AlGoreConditionViQueryService(
        AlGoreConditionViRepository alGoreConditionViRepository,
        AlGoreConditionViMapper alGoreConditionViMapper
    ) {
        this.alGoreConditionViRepository = alGoreConditionViRepository;
        this.alGoreConditionViMapper = alGoreConditionViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlGoreConditionViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlGoreConditionViDTO> findByCriteria(AlGoreConditionViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlGoreConditionVi> specification = createSpecification(criteria);
        return alGoreConditionViRepository.findAll(specification, page).map(alGoreConditionViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlGoreConditionViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlGoreConditionVi> specification = createSpecification(criteria);
        return alGoreConditionViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlGoreConditionViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlGoreConditionVi> createSpecification(AlGoreConditionViCriteria criteria) {
        Specification<AlGoreConditionVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlGoreConditionVi_.id));
            }
            if (criteria.getSubjectType() != null) {
                specification = specification.and(buildSpecification(criteria.getSubjectType(), AlGoreConditionVi_.subjectType));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubject(), AlGoreConditionVi_.subject));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildSpecification(criteria.getAction(), AlGoreConditionVi_.action));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), AlGoreConditionVi_.note));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlGoreConditionVi_.parent, JoinType.LEFT).get(AlGore_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlGoreConditionVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
