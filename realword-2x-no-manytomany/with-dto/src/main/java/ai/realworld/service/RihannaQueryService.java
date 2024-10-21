package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.Rihanna;
import ai.realworld.repository.RihannaRepository;
import ai.realworld.service.criteria.RihannaCriteria;
import ai.realworld.service.dto.RihannaDTO;
import ai.realworld.service.mapper.RihannaMapper;
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
 * Service for executing complex queries for {@link Rihanna} entities in the database.
 * The main input is a {@link RihannaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RihannaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RihannaQueryService extends QueryService<Rihanna> {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaQueryService.class);

    private final RihannaRepository rihannaRepository;

    private final RihannaMapper rihannaMapper;

    public RihannaQueryService(RihannaRepository rihannaRepository, RihannaMapper rihannaMapper) {
        this.rihannaRepository = rihannaRepository;
        this.rihannaMapper = rihannaMapper;
    }

    /**
     * Return a {@link Page} of {@link RihannaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RihannaDTO> findByCriteria(RihannaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rihanna> specification = createSpecification(criteria);
        return rihannaRepository.findAll(specification, page).map(rihannaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RihannaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Rihanna> specification = createSpecification(criteria);
        return rihannaRepository.count(specification);
    }

    /**
     * Function to convert {@link RihannaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rihanna> createSpecification(RihannaCriteria criteria) {
        Specification<Rihanna> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rihanna_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Rihanna_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Rihanna_.description));
            }
            if (criteria.getPermissionGridJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPermissionGridJason(), Rihanna_.permissionGridJason)
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(Rihanna_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAgentRolesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentRolesId(), root -> root.join(Rihanna_.agentRoles, JoinType.LEFT).get(HandCraft_.id))
                );
            }
        }
        return specification;
    }
}
