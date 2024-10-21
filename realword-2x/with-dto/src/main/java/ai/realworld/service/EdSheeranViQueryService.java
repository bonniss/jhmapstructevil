package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.repository.EdSheeranViRepository;
import ai.realworld.service.criteria.EdSheeranViCriteria;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.service.mapper.EdSheeranViMapper;
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
 * Service for executing complex queries for {@link EdSheeranVi} entities in the database.
 * The main input is a {@link EdSheeranViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EdSheeranViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EdSheeranViQueryService extends QueryService<EdSheeranVi> {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranViQueryService.class);

    private final EdSheeranViRepository edSheeranViRepository;

    private final EdSheeranViMapper edSheeranViMapper;

    public EdSheeranViQueryService(EdSheeranViRepository edSheeranViRepository, EdSheeranViMapper edSheeranViMapper) {
        this.edSheeranViRepository = edSheeranViRepository;
        this.edSheeranViMapper = edSheeranViMapper;
    }

    /**
     * Return a {@link Page} of {@link EdSheeranViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EdSheeranViDTO> findByCriteria(EdSheeranViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EdSheeranVi> specification = createSpecification(criteria);
        return edSheeranViRepository.findAll(specification, page).map(edSheeranViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EdSheeranViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EdSheeranVi> specification = createSpecification(criteria);
        return edSheeranViRepository.count(specification);
    }

    /**
     * Function to convert {@link EdSheeranViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EdSheeranVi> createSpecification(EdSheeranViCriteria criteria) {
        Specification<EdSheeranVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EdSheeranVi_.id));
            }
            if (criteria.getFamilyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyName(), EdSheeranVi_.familyName));
            }
            if (criteria.getGivenName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGivenName(), EdSheeranVi_.givenName));
            }
            if (criteria.getDisplay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplay(), EdSheeranVi_.display));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), EdSheeranVi_.dob));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), EdSheeranVi_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), EdSheeranVi_.phone));
            }
            if (criteria.getContactsJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactsJason(), EdSheeranVi_.contactsJason));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), EdSheeranVi_.isEnabled));
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(EdSheeranVi_.agency, JoinType.LEFT).get(AlAppleVi_.id))
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(EdSheeranVi_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root ->
                        root.join(EdSheeranVi_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getAppUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppUserId(), root -> root.join(EdSheeranVi_.appUser, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(EdSheeranVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAgentRolesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentRolesId(), root ->
                        root.join(EdSheeranVi_.agentRoles, JoinType.LEFT).get(HandCraftVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
