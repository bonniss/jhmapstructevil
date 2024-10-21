package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlAlexType;
import ai.realworld.repository.AlAlexTypeRepository;
import ai.realworld.service.criteria.AlAlexTypeCriteria;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.mapper.AlAlexTypeMapper;
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
 * Service for executing complex queries for {@link AlAlexType} entities in the database.
 * The main input is a {@link AlAlexTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlAlexTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlAlexTypeQueryService extends QueryService<AlAlexType> {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeQueryService.class);

    private final AlAlexTypeRepository alAlexTypeRepository;

    private final AlAlexTypeMapper alAlexTypeMapper;

    public AlAlexTypeQueryService(AlAlexTypeRepository alAlexTypeRepository, AlAlexTypeMapper alAlexTypeMapper) {
        this.alAlexTypeRepository = alAlexTypeRepository;
        this.alAlexTypeMapper = alAlexTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link AlAlexTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlAlexTypeDTO> findByCriteria(AlAlexTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlAlexType> specification = createSpecification(criteria);
        return alAlexTypeRepository.findAll(specification, page).map(alAlexTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlAlexTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlAlexType> specification = createSpecification(criteria);
        return alAlexTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AlAlexTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlAlexType> createSpecification(AlAlexTypeCriteria criteria) {
        Specification<AlAlexType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlAlexType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlAlexType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlAlexType_.description));
            }
            if (criteria.getCanDoRetail() != null) {
                specification = specification.and(buildSpecification(criteria.getCanDoRetail(), AlAlexType_.canDoRetail));
            }
            if (criteria.getIsOrgDivision() != null) {
                specification = specification.and(buildSpecification(criteria.getIsOrgDivision(), AlAlexType_.isOrgDivision));
            }
            if (criteria.getConfigJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigJason(), AlAlexType_.configJason));
            }
            if (criteria.getTreeDepth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTreeDepth(), AlAlexType_.treeDepth));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlAlexType_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAsSupplierId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAsSupplierId(), root ->
                        root.join(AlAlexType_.asSuppliers, JoinType.LEFT).get(AlBetonamuRelation_.id)
                    )
                );
            }
            if (criteria.getAsCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAsCustomerId(), root ->
                        root.join(AlAlexType_.asCustomers, JoinType.LEFT).get(AlBetonamuRelation_.id)
                    )
                );
            }
            if (criteria.getAgenciesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgenciesId(), root -> root.join(AlAlexType_.agencies, JoinType.LEFT).get(AlApple_.id))
                );
            }
        }
        return specification;
    }
}
