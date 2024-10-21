package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlApple;
import ai.realworld.repository.AlAppleRepository;
import ai.realworld.service.criteria.AlAppleCriteria;
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
 * Service for executing complex queries for {@link AlApple} entities in the database.
 * The main input is a {@link AlAppleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlApple} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlAppleQueryService extends QueryService<AlApple> {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleQueryService.class);

    private final AlAppleRepository alAppleRepository;

    public AlAppleQueryService(AlAppleRepository alAppleRepository) {
        this.alAppleRepository = alAppleRepository;
    }

    /**
     * Return a {@link Page} of {@link AlApple} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlApple> findByCriteria(AlAppleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlApple> specification = createSpecification(criteria);
        return alAppleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlAppleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlApple> specification = createSpecification(criteria);
        return alAppleRepository.count(specification);
    }

    /**
     * Function to convert {@link AlAppleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlApple> createSpecification(AlAppleCriteria criteria) {
        Specification<AlApple> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlApple_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlApple_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlApple_.description));
            }
            if (criteria.getHotline() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHotline(), AlApple_.hotline));
            }
            if (criteria.getTaxCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxCode(), AlApple_.taxCode));
            }
            if (criteria.getContactsJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactsJason(), AlApple_.contactsJason));
            }
            if (criteria.getExtensionJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtensionJason(), AlApple_.extensionJason));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlApple_.isEnabled));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root -> root.join(AlApple_.address, JoinType.LEFT).get(AndreiRightHand_.id))
                );
            }
            if (criteria.getAgencyTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyTypeId(), root -> root.join(AlApple_.agencyType, JoinType.LEFT).get(AlAlexType_.id)
                    )
                );
            }
            if (criteria.getLogoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLogoId(), root -> root.join(AlApple_.logo, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlApple_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAgentsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentsId(), root -> root.join(AlApple_.agents, JoinType.LEFT).get(EdSheeran_.id))
                );
            }
        }
        return specification;
    }
}
