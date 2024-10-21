package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlBetonamuRelationVi;
import ai.realworld.repository.AlBetonamuRelationViRepository;
import ai.realworld.service.criteria.AlBetonamuRelationViCriteria;
import ai.realworld.service.dto.AlBetonamuRelationViDTO;
import ai.realworld.service.mapper.AlBetonamuRelationViMapper;
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
 * Service for executing complex queries for {@link AlBetonamuRelationVi} entities in the database.
 * The main input is a {@link AlBetonamuRelationViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlBetonamuRelationViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlBetonamuRelationViQueryService extends QueryService<AlBetonamuRelationVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationViQueryService.class);

    private final AlBetonamuRelationViRepository alBetonamuRelationViRepository;

    private final AlBetonamuRelationViMapper alBetonamuRelationViMapper;

    public AlBetonamuRelationViQueryService(
        AlBetonamuRelationViRepository alBetonamuRelationViRepository,
        AlBetonamuRelationViMapper alBetonamuRelationViMapper
    ) {
        this.alBetonamuRelationViRepository = alBetonamuRelationViRepository;
        this.alBetonamuRelationViMapper = alBetonamuRelationViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlBetonamuRelationViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlBetonamuRelationViDTO> findByCriteria(AlBetonamuRelationViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlBetonamuRelationVi> specification = createSpecification(criteria);
        return alBetonamuRelationViRepository.findAll(specification, page).map(alBetonamuRelationViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlBetonamuRelationViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlBetonamuRelationVi> specification = createSpecification(criteria);
        return alBetonamuRelationViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlBetonamuRelationViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlBetonamuRelationVi> createSpecification(AlBetonamuRelationViCriteria criteria) {
        Specification<AlBetonamuRelationVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlBetonamuRelationVi_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), AlBetonamuRelationVi_.type));
            }
            if (criteria.getSupplierId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSupplierId(), root ->
                        root.join(AlBetonamuRelationVi_.supplier, JoinType.LEFT).get(AlAlexTypeVi_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlBetonamuRelationVi_.customer, JoinType.LEFT).get(AlAlexTypeVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlBetonamuRelationVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getDiscountsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDiscountsId(), root ->
                        root.join(AlBetonamuRelationVi_.discounts, JoinType.LEFT).get(AlGore_.id)
                    )
                );
            }
        }
        return specification;
    }
}
