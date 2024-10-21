package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlMenity;
import ai.realworld.repository.AlMenityRepository;
import ai.realworld.service.criteria.AlMenityCriteria;
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
 * Service for executing complex queries for {@link AlMenity} entities in the database.
 * The main input is a {@link AlMenityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlMenity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlMenityQueryService extends QueryService<AlMenity> {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityQueryService.class);

    private final AlMenityRepository alMenityRepository;

    public AlMenityQueryService(AlMenityRepository alMenityRepository) {
        this.alMenityRepository = alMenityRepository;
    }

    /**
     * Return a {@link Page} of {@link AlMenity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlMenity> findByCriteria(AlMenityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlMenity> specification = createSpecification(criteria);
        return alMenityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlMenityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlMenity> specification = createSpecification(criteria);
        return alMenityRepository.count(specification);
    }

    /**
     * Function to convert {@link AlMenityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlMenity> createSpecification(AlMenityCriteria criteria) {
        Specification<AlMenity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlMenity_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlMenity_.name));
            }
            if (criteria.getIconSvg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIconSvg(), AlMenity_.iconSvg));
            }
            if (criteria.getPropertyType() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertyType(), AlMenity_.propertyType));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlMenity_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
