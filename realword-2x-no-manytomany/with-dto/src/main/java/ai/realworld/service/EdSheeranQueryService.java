package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.EdSheeran;
import ai.realworld.repository.EdSheeranRepository;
import ai.realworld.service.criteria.EdSheeranCriteria;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.mapper.EdSheeranMapper;
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
 * Service for executing complex queries for {@link EdSheeran} entities in the database.
 * The main input is a {@link EdSheeranCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EdSheeranDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EdSheeranQueryService extends QueryService<EdSheeran> {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranQueryService.class);

    private final EdSheeranRepository edSheeranRepository;

    private final EdSheeranMapper edSheeranMapper;

    public EdSheeranQueryService(EdSheeranRepository edSheeranRepository, EdSheeranMapper edSheeranMapper) {
        this.edSheeranRepository = edSheeranRepository;
        this.edSheeranMapper = edSheeranMapper;
    }

    /**
     * Return a {@link Page} of {@link EdSheeranDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EdSheeranDTO> findByCriteria(EdSheeranCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EdSheeran> specification = createSpecification(criteria);
        return edSheeranRepository.findAll(specification, page).map(edSheeranMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EdSheeranCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EdSheeran> specification = createSpecification(criteria);
        return edSheeranRepository.count(specification);
    }

    /**
     * Function to convert {@link EdSheeranCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EdSheeran> createSpecification(EdSheeranCriteria criteria) {
        Specification<EdSheeran> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EdSheeran_.id));
            }
            if (criteria.getFamilyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyName(), EdSheeran_.familyName));
            }
            if (criteria.getGivenName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGivenName(), EdSheeran_.givenName));
            }
            if (criteria.getDisplay() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisplay(), EdSheeran_.display));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), EdSheeran_.dob));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), EdSheeran_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), EdSheeran_.phone));
            }
            if (criteria.getContactsJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactsJason(), EdSheeran_.contactsJason));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), EdSheeran_.isEnabled));
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(EdSheeran_.agency, JoinType.LEFT).get(AlApple_.id))
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(EdSheeran_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root -> root.join(EdSheeran_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getAppUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppUserId(), root -> root.join(EdSheeran_.appUser, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(EdSheeran_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAgentRolesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentRolesId(), root ->
                        root.join(EdSheeran_.agentRoles, JoinType.LEFT).get(HandCraft_.id)
                    )
                );
            }
        }
        return specification;
    }
}
