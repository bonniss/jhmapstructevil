package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.HandCraftVi;
import ai.realworld.repository.HandCraftViRepository;
import ai.realworld.service.criteria.HandCraftViCriteria;
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
 * Service for executing complex queries for {@link HandCraftVi} entities in the database.
 * The main input is a {@link HandCraftViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HandCraftVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HandCraftViQueryService extends QueryService<HandCraftVi> {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftViQueryService.class);

    private final HandCraftViRepository handCraftViRepository;

    public HandCraftViQueryService(HandCraftViRepository handCraftViRepository) {
        this.handCraftViRepository = handCraftViRepository;
    }

    /**
     * Return a {@link Page} of {@link HandCraftVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HandCraftVi> findByCriteria(HandCraftViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HandCraftVi> specification = createSpecification(criteria);
        return handCraftViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HandCraftViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HandCraftVi> specification = createSpecification(criteria);
        return handCraftViRepository.count(specification);
    }

    /**
     * Function to convert {@link HandCraftViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HandCraftVi> createSpecification(HandCraftViCriteria criteria) {
        Specification<HandCraftVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HandCraftVi_.id));
            }
            if (criteria.getAgentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentId(), root -> root.join(HandCraftVi_.agent, JoinType.LEFT).get(EdSheeranVi_.id))
                );
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoleId(), root -> root.join(HandCraftVi_.role, JoinType.LEFT).get(RihannaVi_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(HandCraftVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
