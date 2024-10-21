package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.repository.AlBetonamuRelationRepository;
import ai.realworld.service.criteria.AlBetonamuRelationCriteria;
import ai.realworld.service.dto.AlBetonamuRelationDTO;
import ai.realworld.service.mapper.AlBetonamuRelationMapper;
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
 * Service for executing complex queries for {@link AlBetonamuRelation} entities in the database.
 * The main input is a {@link AlBetonamuRelationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlBetonamuRelationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlBetonamuRelationQueryService extends QueryService<AlBetonamuRelation> {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationQueryService.class);

    private final AlBetonamuRelationRepository alBetonamuRelationRepository;

    private final AlBetonamuRelationMapper alBetonamuRelationMapper;

    public AlBetonamuRelationQueryService(
        AlBetonamuRelationRepository alBetonamuRelationRepository,
        AlBetonamuRelationMapper alBetonamuRelationMapper
    ) {
        this.alBetonamuRelationRepository = alBetonamuRelationRepository;
        this.alBetonamuRelationMapper = alBetonamuRelationMapper;
    }

    /**
     * Return a {@link Page} of {@link AlBetonamuRelationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlBetonamuRelationDTO> findByCriteria(AlBetonamuRelationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlBetonamuRelation> specification = createSpecification(criteria);
        return alBetonamuRelationRepository.findAll(specification, page).map(alBetonamuRelationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlBetonamuRelationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlBetonamuRelation> specification = createSpecification(criteria);
        return alBetonamuRelationRepository.count(specification);
    }

    /**
     * Function to convert {@link AlBetonamuRelationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlBetonamuRelation> createSpecification(AlBetonamuRelationCriteria criteria) {
        Specification<AlBetonamuRelation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlBetonamuRelation_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), AlBetonamuRelation_.type));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSupplierId(), root ->
                        root.join(AlBetonamuRelation_.supplier, JoinType.LEFT).get(AlAlexType_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlBetonamuRelation_.customer, JoinType.LEFT).get(AlAlexType_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlBetonamuRelation_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getDiscountsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDiscountsId(), root ->
                        root.join(AlBetonamuRelation_.discounts, JoinType.LEFT).get(AlGore_.id)
                    )
                );
            }
        }
        return specification;
    }
}
