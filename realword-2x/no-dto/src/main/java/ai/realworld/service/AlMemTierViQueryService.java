package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlMemTierVi;
import ai.realworld.repository.AlMemTierViRepository;
import ai.realworld.service.criteria.AlMemTierViCriteria;
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
 * Service for executing complex queries for {@link AlMemTierVi} entities in the database.
 * The main input is a {@link AlMemTierViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlMemTierVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlMemTierViQueryService extends QueryService<AlMemTierVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierViQueryService.class);

    private final AlMemTierViRepository alMemTierViRepository;

    public AlMemTierViQueryService(AlMemTierViRepository alMemTierViRepository) {
        this.alMemTierViRepository = alMemTierViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlMemTierVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlMemTierVi> findByCriteria(AlMemTierViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlMemTierVi> specification = createSpecification(criteria);
        return alMemTierViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlMemTierViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlMemTierVi> specification = createSpecification(criteria);
        return alMemTierViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlMemTierViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlMemTierVi> createSpecification(AlMemTierViCriteria criteria) {
        Specification<AlMemTierVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlMemTierVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlMemTierVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlMemTierVi_.description));
            }
            if (criteria.getMinPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinPoint(), AlMemTierVi_.minPoint));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlMemTierVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
