package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.repository.AntonioBanderasViRepository;
import ai.realworld.service.criteria.AntonioBanderasViCriteria;
import ai.realworld.service.dto.AntonioBanderasViDTO;
import ai.realworld.service.mapper.AntonioBanderasViMapper;
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
 * Service for executing complex queries for {@link AntonioBanderasVi} entities in the database.
 * The main input is a {@link AntonioBanderasViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AntonioBanderasViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AntonioBanderasViQueryService extends QueryService<AntonioBanderasVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasViQueryService.class);

    private final AntonioBanderasViRepository antonioBanderasViRepository;

    private final AntonioBanderasViMapper antonioBanderasViMapper;

    public AntonioBanderasViQueryService(
        AntonioBanderasViRepository antonioBanderasViRepository,
        AntonioBanderasViMapper antonioBanderasViMapper
    ) {
        this.antonioBanderasViRepository = antonioBanderasViRepository;
        this.antonioBanderasViMapper = antonioBanderasViMapper;
    }

    /**
     * Return a {@link Page} of {@link AntonioBanderasViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AntonioBanderasViDTO> findByCriteria(AntonioBanderasViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AntonioBanderasVi> specification = createSpecification(criteria);
        return antonioBanderasViRepository.findAll(specification, page).map(antonioBanderasViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AntonioBanderasViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AntonioBanderasVi> specification = createSpecification(criteria);
        return antonioBanderasViRepository.count(specification);
    }

    /**
     * Function to convert {@link AntonioBanderasViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AntonioBanderasVi> createSpecification(AntonioBanderasViCriteria criteria) {
        Specification<AntonioBanderasVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AntonioBanderasVi_.id));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), AntonioBanderasVi_.level));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AntonioBanderasVi_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AntonioBanderasVi_.name));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), AntonioBanderasVi_.fullName));
            }
            if (criteria.getNativeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNativeName(), AntonioBanderasVi_.nativeName));
            }
            if (criteria.getOfficialCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficialCode(), AntonioBanderasVi_.officialCode));
            }
            if (criteria.getDivisionTerm() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivisionTerm(), AntonioBanderasVi_.divisionTerm));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), AntonioBanderasVi_.isDeleted));
            }
            if (criteria.getCurrentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCurrentId(), root ->
                        root.join(AntonioBanderasVi_.current, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root ->
                        root.join(AntonioBanderasVi_.parent, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root ->
                        root.join(AntonioBanderasVi_.children, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
            if (criteria.getAntonioBanderasViId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAntonioBanderasViId(), root ->
                        root.join(AntonioBanderasVi_.antonioBanderasVi, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
