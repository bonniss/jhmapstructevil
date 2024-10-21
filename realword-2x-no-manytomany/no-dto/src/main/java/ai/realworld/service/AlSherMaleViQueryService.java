package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlSherMaleVi;
import ai.realworld.repository.AlSherMaleViRepository;
import ai.realworld.service.criteria.AlSherMaleViCriteria;
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
 * Service for executing complex queries for {@link AlSherMaleVi} entities in the database.
 * The main input is a {@link AlSherMaleViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlSherMaleVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlSherMaleViQueryService extends QueryService<AlSherMaleVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleViQueryService.class);

    private final AlSherMaleViRepository alSherMaleViRepository;

    public AlSherMaleViQueryService(AlSherMaleViRepository alSherMaleViRepository) {
        this.alSherMaleViRepository = alSherMaleViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlSherMaleVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlSherMaleVi> findByCriteria(AlSherMaleViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlSherMaleVi> specification = createSpecification(criteria);
        return alSherMaleViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlSherMaleViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlSherMaleVi> specification = createSpecification(criteria);
        return alSherMaleViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlSherMaleViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlSherMaleVi> createSpecification(AlSherMaleViCriteria criteria) {
        Specification<AlSherMaleVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlSherMaleVi_.id));
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AlSherMaleVi_.dataSourceMappingType)
                );
            }
            if (criteria.getKeyword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeyword(), AlSherMaleVi_.keyword));
            }
            if (criteria.getProperty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProperty(), AlSherMaleVi_.property));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlSherMaleVi_.title));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlSherMaleVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
