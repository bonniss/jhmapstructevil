package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.OlMaster;
import ai.realworld.repository.OlMasterRepository;
import ai.realworld.service.criteria.OlMasterCriteria;
import ai.realworld.service.dto.OlMasterDTO;
import ai.realworld.service.mapper.OlMasterMapper;
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
 * Service for executing complex queries for {@link OlMaster} entities in the database.
 * The main input is a {@link OlMasterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OlMasterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OlMasterQueryService extends QueryService<OlMaster> {

    private static final Logger LOG = LoggerFactory.getLogger(OlMasterQueryService.class);

    private final OlMasterRepository olMasterRepository;

    private final OlMasterMapper olMasterMapper;

    public OlMasterQueryService(OlMasterRepository olMasterRepository, OlMasterMapper olMasterMapper) {
        this.olMasterRepository = olMasterRepository;
        this.olMasterMapper = olMasterMapper;
    }

    /**
     * Return a {@link Page} of {@link OlMasterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OlMasterDTO> findByCriteria(OlMasterCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OlMaster> specification = createSpecification(criteria);
        return olMasterRepository.findAll(specification, page).map(olMasterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OlMasterCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OlMaster> specification = createSpecification(criteria);
        return olMasterRepository.count(specification);
    }

    /**
     * Function to convert {@link OlMasterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OlMaster> createSpecification(OlMasterCriteria criteria) {
        Specification<OlMaster> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OlMaster_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OlMaster_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), OlMaster_.slug));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescriptionHeitiga(), OlMaster_.descriptionHeitiga));
            }
            if (criteria.getBusinessType() != null) {
                specification = specification.and(buildSpecification(criteria.getBusinessType(), OlMaster_.businessType));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), OlMaster_.email));
            }
            if (criteria.getHotline() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHotline(), OlMaster_.hotline));
            }
            if (criteria.getTaxCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxCode(), OlMaster_.taxCode));
            }
            if (criteria.getContactsJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactsJason(), OlMaster_.contactsJason));
            }
            if (criteria.getExtensionJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtensionJason(), OlMaster_.extensionJason));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), OlMaster_.isEnabled));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root -> root.join(OlMaster_.address, JoinType.LEFT).get(AndreiRightHand_.id)
                    )
                );
            }
            if (criteria.getApplicationsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationsId(), root ->
                        root.join(OlMaster_.applications, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
