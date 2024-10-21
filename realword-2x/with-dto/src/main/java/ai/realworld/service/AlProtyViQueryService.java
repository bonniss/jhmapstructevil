package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlProtyVi;
import ai.realworld.repository.AlProtyViRepository;
import ai.realworld.service.criteria.AlProtyViCriteria;
import ai.realworld.service.dto.AlProtyViDTO;
import ai.realworld.service.mapper.AlProtyViMapper;
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
 * Service for executing complex queries for {@link AlProtyVi} entities in the database.
 * The main input is a {@link AlProtyViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlProtyViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlProtyViQueryService extends QueryService<AlProtyVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyViQueryService.class);

    private final AlProtyViRepository alProtyViRepository;

    private final AlProtyViMapper alProtyViMapper;

    public AlProtyViQueryService(AlProtyViRepository alProtyViRepository, AlProtyViMapper alProtyViMapper) {
        this.alProtyViRepository = alProtyViRepository;
        this.alProtyViMapper = alProtyViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlProtyViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlProtyViDTO> findByCriteria(AlProtyViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlProtyVi> specification = createSpecification(criteria);
        return alProtyViRepository.fetchBagRelationships(alProtyViRepository.findAll(specification, page)).map(alProtyViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlProtyViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlProtyVi> specification = createSpecification(criteria);
        return alProtyViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlProtyViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlProtyVi> createSpecification(AlProtyViCriteria criteria) {
        Specification<AlProtyVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlProtyVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlProtyVi_.name));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionHeitiga(), AlProtyVi_.descriptionHeitiga)
                );
            }
            if (criteria.getCoordinate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoordinate(), AlProtyVi_.coordinate));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AlProtyVi_.code));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), AlProtyVi_.status));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlProtyVi_.isEnabled));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlProtyVi_.parent, JoinType.LEFT).get(AlProtyVi_.id))
                );
            }
            if (criteria.getOperatorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOperatorId(), root -> root.join(AlProtyVi_.operator, JoinType.LEFT).get(AlAppleVi_.id))
                );
            }
            if (criteria.getPropertyProfileId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPropertyProfileId(), root ->
                        root.join(AlProtyVi_.propertyProfile, JoinType.LEFT).get(AlProProVi_.id)
                    )
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlProtyVi_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlProtyVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlProtyVi_.images, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root -> root.join(AlProtyVi_.children, JoinType.LEFT).get(AlProtyVi_.id))
                );
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBookingId(), root -> root.join(AlProtyVi_.bookings, JoinType.LEFT).get(AlPyuJokerVi_.id))
                );
            }
        }
        return specification;
    }
}
