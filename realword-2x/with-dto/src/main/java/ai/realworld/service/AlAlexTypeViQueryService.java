package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.repository.AlAlexTypeViRepository;
import ai.realworld.service.criteria.AlAlexTypeViCriteria;
import ai.realworld.service.dto.AlAlexTypeViDTO;
import ai.realworld.service.mapper.AlAlexTypeViMapper;
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
 * Service for executing complex queries for {@link AlAlexTypeVi} entities in the database.
 * The main input is a {@link AlAlexTypeViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlAlexTypeViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlAlexTypeViQueryService extends QueryService<AlAlexTypeVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeViQueryService.class);

    private final AlAlexTypeViRepository alAlexTypeViRepository;

    private final AlAlexTypeViMapper alAlexTypeViMapper;

    public AlAlexTypeViQueryService(AlAlexTypeViRepository alAlexTypeViRepository, AlAlexTypeViMapper alAlexTypeViMapper) {
        this.alAlexTypeViRepository = alAlexTypeViRepository;
        this.alAlexTypeViMapper = alAlexTypeViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlAlexTypeViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlAlexTypeViDTO> findByCriteria(AlAlexTypeViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlAlexTypeVi> specification = createSpecification(criteria);
        return alAlexTypeViRepository.findAll(specification, page).map(alAlexTypeViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlAlexTypeViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlAlexTypeVi> specification = createSpecification(criteria);
        return alAlexTypeViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlAlexTypeViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlAlexTypeVi> createSpecification(AlAlexTypeViCriteria criteria) {
        Specification<AlAlexTypeVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlAlexTypeVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlAlexTypeVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlAlexTypeVi_.description));
            }
            if (criteria.getCanDoRetail() != null) {
                specification = specification.and(buildSpecification(criteria.getCanDoRetail(), AlAlexTypeVi_.canDoRetail));
            }
            if (criteria.getIsOrgDivision() != null) {
                specification = specification.and(buildSpecification(criteria.getIsOrgDivision(), AlAlexTypeVi_.isOrgDivision));
            }
            if (criteria.getConfigJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigJason(), AlAlexTypeVi_.configJason));
            }
            if (criteria.getTreeDepth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTreeDepth(), AlAlexTypeVi_.treeDepth));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlAlexTypeVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAsSupplierId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAsSupplierId(), root ->
                        root.join(AlAlexTypeVi_.asSuppliers, JoinType.LEFT).get(AlBetonamuRelationVi_.id)
                    )
                );
            }
            if (criteria.getAsCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAsCustomerId(), root ->
                        root.join(AlAlexTypeVi_.asCustomers, JoinType.LEFT).get(AlBetonamuRelationVi_.id)
                    )
                );
            }
            if (criteria.getAgenciesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgenciesId(), root -> root.join(AlAlexTypeVi_.agencies, JoinType.LEFT).get(AlAppleVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
