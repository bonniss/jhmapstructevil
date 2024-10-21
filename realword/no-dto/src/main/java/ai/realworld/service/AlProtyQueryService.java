package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlProty;
import ai.realworld.repository.AlProtyRepository;
import ai.realworld.service.criteria.AlProtyCriteria;
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
 * Service for executing complex queries for {@link AlProty} entities in the database.
 * The main input is a {@link AlProtyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlProty} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlProtyQueryService extends QueryService<AlProty> {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyQueryService.class);

    private final AlProtyRepository alProtyRepository;

    public AlProtyQueryService(AlProtyRepository alProtyRepository) {
        this.alProtyRepository = alProtyRepository;
    }

    /**
     * Return a {@link Page} of {@link AlProty} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlProty> findByCriteria(AlProtyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlProty> specification = createSpecification(criteria);
        return alProtyRepository.fetchBagRelationships(alProtyRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlProtyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlProty> specification = createSpecification(criteria);
        return alProtyRepository.count(specification);
    }

    /**
     * Function to convert {@link AlProtyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlProty> createSpecification(AlProtyCriteria criteria) {
        Specification<AlProty> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlProty_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlProty_.name));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescriptionHeitiga(), AlProty_.descriptionHeitiga));
            }
            if (criteria.getCoordinate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoordinate(), AlProty_.coordinate));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AlProty_.code));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AlProty_.status));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlProty_.isEnabled));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlProty_.parent, JoinType.LEFT).get(AlProty_.id))
                );
            }
            if (criteria.getOperatorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOperatorId(), root -> root.join(AlProty_.operator, JoinType.LEFT).get(AlApple_.id))
                );
            }
            if (criteria.getPropertyProfileId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPropertyProfileId(), root ->
                        root.join(AlProty_.propertyProfile, JoinType.LEFT).get(AlProPro_.id)
                    )
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlProty_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlProty_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlProty_.images, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root -> root.join(AlProty_.children, JoinType.LEFT).get(AlProty_.id))
                );
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBookingId(), root -> root.join(AlProty_.bookings, JoinType.LEFT).get(AlPyuJoker_.id))
                );
            }
        }
        return specification;
    }
}
