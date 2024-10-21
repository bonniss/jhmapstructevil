package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.JohnLennonRepository;
import ai.realworld.service.criteria.JohnLennonCriteria;
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
 * Service for executing complex queries for {@link JohnLennon} entities in the database.
 * The main input is a {@link JohnLennonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link JohnLennon} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JohnLennonQueryService extends QueryService<JohnLennon> {

    private static final Logger LOG = LoggerFactory.getLogger(JohnLennonQueryService.class);

    private final JohnLennonRepository johnLennonRepository;

    public JohnLennonQueryService(JohnLennonRepository johnLennonRepository) {
        this.johnLennonRepository = johnLennonRepository;
    }

    /**
     * Return a {@link Page} of {@link JohnLennon} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JohnLennon> findByCriteria(JohnLennonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JohnLennon> specification = createSpecification(criteria);
        return johnLennonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JohnLennonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<JohnLennon> specification = createSpecification(criteria);
        return johnLennonRepository.count(specification);
    }

    /**
     * Function to convert {@link JohnLennonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JohnLennon> createSpecification(JohnLennonCriteria criteria) {
        Specification<JohnLennon> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JohnLennon_.id));
            }
            if (criteria.getProvider() != null) {
                specification = specification.and(buildSpecification(criteria.getProvider(), JohnLennon_.provider));
            }
            if (criteria.getProviderAppId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProviderAppId(), JohnLennon_.providerAppId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), JohnLennon_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), JohnLennon_.slug));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), JohnLennon_.isEnabled));
            }
            if (criteria.getLogoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLogoId(), root -> root.join(JohnLennon_.logo, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getAppManagerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppManagerId(), root ->
                        root.join(JohnLennon_.appManager, JoinType.LEFT).get(OlAlmantinoMilo_.id)
                    )
                );
            }
            if (criteria.getOrganizationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrganizationId(), root ->
                        root.join(JohnLennon_.organization, JoinType.LEFT).get(OlMaster_.id)
                    )
                );
            }
            if (criteria.getJelloInitiumId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJelloInitiumId(), root ->
                        root.join(JohnLennon_.jelloInitium, JoinType.LEFT).get(Initium_.id)
                    )
                );
            }
            if (criteria.getInhouseInitiumId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInhouseInitiumId(), root ->
                        root.join(JohnLennon_.inhouseInitium, JoinType.LEFT).get(Initium_.id)
                    )
                );
            }
            if (criteria.getJelloInitiumViId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJelloInitiumViId(), root ->
                        root.join(JohnLennon_.jelloInitiumVi, JoinType.LEFT).get(InitiumVi_.id)
                    )
                );
            }
            if (criteria.getInhouseInitiumViId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInhouseInitiumViId(), root ->
                        root.join(JohnLennon_.inhouseInitiumVi, JoinType.LEFT).get(InitiumVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
