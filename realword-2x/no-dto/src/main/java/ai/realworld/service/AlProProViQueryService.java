package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlProProVi;
import ai.realworld.repository.AlProProViRepository;
import ai.realworld.service.criteria.AlProProViCriteria;
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
 * Service for executing complex queries for {@link AlProProVi} entities in the database.
 * The main input is a {@link AlProProViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlProProVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlProProViQueryService extends QueryService<AlProProVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProViQueryService.class);

    private final AlProProViRepository alProProViRepository;

    public AlProProViQueryService(AlProProViRepository alProProViRepository) {
        this.alProProViRepository = alProProViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlProProVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlProProVi> findByCriteria(AlProProViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlProProVi> specification = createSpecification(criteria);
        return alProProViRepository.fetchBagRelationships(alProProViRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlProProViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlProProVi> specification = createSpecification(criteria);
        return alProProViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlProProViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlProProVi> createSpecification(AlProProViCriteria criteria) {
        Specification<AlProProVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlProProVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlProProVi_.name));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionHeitiga(), AlProProVi_.descriptionHeitiga)
                );
            }
            if (criteria.getPropertyType() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertyType(), AlProProVi_.propertyType));
            }
            if (criteria.getAreaInSquareMeter() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAreaInSquareMeter(), AlProProVi_.areaInSquareMeter));
            }
            if (criteria.getNumberOfAdults() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfAdults(), AlProProVi_.numberOfAdults));
            }
            if (criteria.getNumberOfPreschoolers() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNumberOfPreschoolers(), AlProProVi_.numberOfPreschoolers)
                );
            }
            if (criteria.getNumberOfChildren() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfChildren(), AlProProVi_.numberOfChildren));
            }
            if (criteria.getNumberOfRooms() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfRooms(), AlProProVi_.numberOfRooms));
            }
            if (criteria.getNumberOfFloors() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfFloors(), AlProProVi_.numberOfFloors));
            }
            if (criteria.getBedSize() != null) {
                specification = specification.and(buildSpecification(criteria.getBedSize(), AlProProVi_.bedSize));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlProProVi_.isEnabled));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlProProVi_.parent, JoinType.LEFT).get(AlProProVi_.id))
                );
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectId(), root -> root.join(AlProProVi_.project, JoinType.LEFT).get(AlLadyGagaVi_.id))
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlProProVi_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlProProVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAmenityId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAmenityId(), root -> root.join(AlProProVi_.amenities, JoinType.LEFT).get(AlMenityVi_.id))
                );
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlProProVi_.images, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root -> root.join(AlProProVi_.children, JoinType.LEFT).get(AlProProVi_.id))
                );
            }
        }
        return specification;
    }
}
