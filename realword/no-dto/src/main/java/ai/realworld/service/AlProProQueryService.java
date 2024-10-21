package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlProPro;
import ai.realworld.repository.AlProProRepository;
import ai.realworld.service.criteria.AlProProCriteria;
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
 * Service for executing complex queries for {@link AlProPro} entities in the database.
 * The main input is a {@link AlProProCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlProPro} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlProProQueryService extends QueryService<AlProPro> {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProQueryService.class);

    private final AlProProRepository alProProRepository;

    public AlProProQueryService(AlProProRepository alProProRepository) {
        this.alProProRepository = alProProRepository;
    }

    /**
     * Return a {@link Page} of {@link AlProPro} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlProPro> findByCriteria(AlProProCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlProPro> specification = createSpecification(criteria);
        return alProProRepository.fetchBagRelationships(alProProRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlProProCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlProPro> specification = createSpecification(criteria);
        return alProProRepository.count(specification);
    }

    /**
     * Function to convert {@link AlProProCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlProPro> createSpecification(AlProProCriteria criteria) {
        Specification<AlProPro> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlProPro_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlProPro_.name));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescriptionHeitiga(), AlProPro_.descriptionHeitiga));
            }
            if (criteria.getPropertyType() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertyType(), AlProPro_.propertyType));
            }
            if (criteria.getAreaInSquareMeter() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAreaInSquareMeter(), AlProPro_.areaInSquareMeter));
            }
            if (criteria.getNumberOfAdults() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfAdults(), AlProPro_.numberOfAdults));
            }
            if (criteria.getNumberOfPreschoolers() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNumberOfPreschoolers(), AlProPro_.numberOfPreschoolers)
                );
            }
            if (criteria.getNumberOfChildren() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfChildren(), AlProPro_.numberOfChildren));
            }
            if (criteria.getNumberOfRooms() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfRooms(), AlProPro_.numberOfRooms));
            }
            if (criteria.getNumberOfFloors() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfFloors(), AlProPro_.numberOfFloors));
            }
            if (criteria.getBedSize() != null) {
                specification = specification.and(buildSpecification(criteria.getBedSize(), AlProPro_.bedSize));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlProPro_.isEnabled));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlProPro_.parent, JoinType.LEFT).get(AlProPro_.id))
                );
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectId(), root -> root.join(AlProPro_.project, JoinType.LEFT).get(AlLadyGaga_.id))
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlProPro_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlProPro_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAmenityId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAmenityId(), root -> root.join(AlProPro_.amenities, JoinType.LEFT).get(AlMenity_.id))
                );
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlProPro_.images, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root -> root.join(AlProPro_.children, JoinType.LEFT).get(AlProPro_.id))
                );
            }
        }
        return specification;
    }
}
