package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPowerShell;
import ai.realworld.repository.AlPowerShellRepository;
import ai.realworld.service.criteria.AlPowerShellCriteria;
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
 * Service for executing complex queries for {@link AlPowerShell} entities in the database.
 * The main input is a {@link AlPowerShellCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPowerShell} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPowerShellQueryService extends QueryService<AlPowerShell> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellQueryService.class);

    private final AlPowerShellRepository alPowerShellRepository;

    public AlPowerShellQueryService(AlPowerShellRepository alPowerShellRepository) {
        this.alPowerShellRepository = alPowerShellRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPowerShell} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPowerShell> findByCriteria(AlPowerShellCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPowerShell> specification = createSpecification(criteria);
        return alPowerShellRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPowerShellCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPowerShell> specification = createSpecification(criteria);
        return alPowerShellRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPowerShellCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPowerShell> createSpecification(AlPowerShellCriteria criteria) {
        Specification<AlPowerShell> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPowerShell_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), AlPowerShell_.value));
            }
            if (criteria.getPropertyProfileId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPropertyProfileId(), root ->
                        root.join(AlPowerShell_.propertyProfile, JoinType.LEFT).get(AlProPro_.id)
                    )
                );
            }
            if (criteria.getAttributeTermId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeTermId(), root ->
                        root.join(AlPowerShell_.attributeTerm, JoinType.LEFT).get(AlPounder_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPowerShell_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
