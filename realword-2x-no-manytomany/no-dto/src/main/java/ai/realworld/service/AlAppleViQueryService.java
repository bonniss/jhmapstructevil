package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlAppleVi;
import ai.realworld.repository.AlAppleViRepository;
import ai.realworld.service.criteria.AlAppleViCriteria;
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
 * Service for executing complex queries for {@link AlAppleVi} entities in the database.
 * The main input is a {@link AlAppleViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlAppleVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlAppleViQueryService extends QueryService<AlAppleVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleViQueryService.class);

    private final AlAppleViRepository alAppleViRepository;

    public AlAppleViQueryService(AlAppleViRepository alAppleViRepository) {
        this.alAppleViRepository = alAppleViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlAppleVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlAppleVi> findByCriteria(AlAppleViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlAppleVi> specification = createSpecification(criteria);
        return alAppleViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlAppleViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlAppleVi> specification = createSpecification(criteria);
        return alAppleViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlAppleViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlAppleVi> createSpecification(AlAppleViCriteria criteria) {
        Specification<AlAppleVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlAppleVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlAppleVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlAppleVi_.description));
            }
            if (criteria.getHotline() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHotline(), AlAppleVi_.hotline));
            }
            if (criteria.getTaxCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxCode(), AlAppleVi_.taxCode));
            }
            if (criteria.getContactsJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactsJason(), AlAppleVi_.contactsJason));
            }
            if (criteria.getExtensionJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExtensionJason(), AlAppleVi_.extensionJason));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlAppleVi_.isEnabled));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root ->
                        root.join(AlAppleVi_.address, JoinType.LEFT).get(AndreiRightHandVi_.id)
                    )
                );
            }
            if (criteria.getAgencyTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyTypeId(), root ->
                        root.join(AlAppleVi_.agencyType, JoinType.LEFT).get(AlAlexTypeVi_.id)
                    )
                );
            }
            if (criteria.getLogoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLogoId(), root -> root.join(AlAppleVi_.logo, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlAppleVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAgentsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentsId(), root -> root.join(AlAppleVi_.agents, JoinType.LEFT).get(EdSheeranVi_.id))
                );
            }
        }
        return specification;
    }
}
