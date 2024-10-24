package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.RihannaVi;
import ai.realworld.repository.RihannaViRepository;
import ai.realworld.service.criteria.RihannaViCriteria;
import ai.realworld.service.dto.RihannaViDTO;
import ai.realworld.service.mapper.RihannaViMapper;
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
 * Service for executing complex queries for {@link RihannaVi} entities in the database.
 * The main input is a {@link RihannaViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RihannaViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RihannaViQueryService extends QueryService<RihannaVi> {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaViQueryService.class);

    private final RihannaViRepository rihannaViRepository;

    private final RihannaViMapper rihannaViMapper;

    public RihannaViQueryService(RihannaViRepository rihannaViRepository, RihannaViMapper rihannaViMapper) {
        this.rihannaViRepository = rihannaViRepository;
        this.rihannaViMapper = rihannaViMapper;
    }

    /**
     * Return a {@link Page} of {@link RihannaViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RihannaViDTO> findByCriteria(RihannaViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RihannaVi> specification = createSpecification(criteria);
        return rihannaViRepository.findAll(specification, page).map(rihannaViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RihannaViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RihannaVi> specification = createSpecification(criteria);
        return rihannaViRepository.count(specification);
    }

    /**
     * Function to convert {@link RihannaViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RihannaVi> createSpecification(RihannaViCriteria criteria) {
        Specification<RihannaVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RihannaVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), RihannaVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RihannaVi_.description));
            }
            if (criteria.getPermissionGridJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPermissionGridJason(), RihannaVi_.permissionGridJason)
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(RihannaVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAgentRolesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentRolesId(), root ->
                        root.join(RihannaVi_.agentRoles, JoinType.LEFT).get(HandCraftVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
