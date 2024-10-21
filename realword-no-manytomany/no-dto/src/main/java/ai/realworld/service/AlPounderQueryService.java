package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPounder;
import ai.realworld.repository.AlPounderRepository;
import ai.realworld.service.criteria.AlPounderCriteria;
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
 * Service for executing complex queries for {@link AlPounder} entities in the database.
 * The main input is a {@link AlPounderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPounder} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPounderQueryService extends QueryService<AlPounder> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderQueryService.class);

    private final AlPounderRepository alPounderRepository;

    public AlPounderQueryService(AlPounderRepository alPounderRepository) {
        this.alPounderRepository = alPounderRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPounder} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPounder> findByCriteria(AlPounderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPounder> specification = createSpecification(criteria);
        return alPounderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPounderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPounder> specification = createSpecification(criteria);
        return alPounderRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPounderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPounder> createSpecification(AlPounderCriteria criteria) {
        Specification<AlPounder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPounder_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlPounder_.name));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlPounder_.weight));
            }
            if (criteria.getAttributeTaxonomyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeTaxonomyId(), root ->
                        root.join(AlPounder_.attributeTaxonomy, JoinType.LEFT).get(AlPedroTax_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPounder_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
