package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.HandCraft;
import ai.realworld.repository.HandCraftRepository;
import ai.realworld.service.criteria.HandCraftCriteria;
import ai.realworld.service.dto.HandCraftDTO;
import ai.realworld.service.mapper.HandCraftMapper;
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
 * Service for executing complex queries for {@link HandCraft} entities in the database.
 * The main input is a {@link HandCraftCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HandCraftDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HandCraftQueryService extends QueryService<HandCraft> {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftQueryService.class);

    private final HandCraftRepository handCraftRepository;

    private final HandCraftMapper handCraftMapper;

    public HandCraftQueryService(HandCraftRepository handCraftRepository, HandCraftMapper handCraftMapper) {
        this.handCraftRepository = handCraftRepository;
        this.handCraftMapper = handCraftMapper;
    }

    /**
     * Return a {@link Page} of {@link HandCraftDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HandCraftDTO> findByCriteria(HandCraftCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HandCraft> specification = createSpecification(criteria);
        return handCraftRepository.findAll(specification, page).map(handCraftMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HandCraftCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HandCraft> specification = createSpecification(criteria);
        return handCraftRepository.count(specification);
    }

    /**
     * Function to convert {@link HandCraftCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HandCraft> createSpecification(HandCraftCriteria criteria) {
        Specification<HandCraft> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HandCraft_.id));
            }
            if (criteria.getAgentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgentId(), root -> root.join(HandCraft_.agent, JoinType.LEFT).get(EdSheeran_.id))
                );
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoleId(), root -> root.join(HandCraft_.role, JoinType.LEFT).get(Rihanna_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(HandCraft_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
