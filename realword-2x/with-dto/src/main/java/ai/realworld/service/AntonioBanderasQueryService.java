package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AntonioBanderas;
import ai.realworld.repository.AntonioBanderasRepository;
import ai.realworld.service.criteria.AntonioBanderasCriteria;
import ai.realworld.service.dto.AntonioBanderasDTO;
import ai.realworld.service.mapper.AntonioBanderasMapper;
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
 * Service for executing complex queries for {@link AntonioBanderas} entities in the database.
 * The main input is a {@link AntonioBanderasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AntonioBanderasDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AntonioBanderasQueryService extends QueryService<AntonioBanderas> {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasQueryService.class);

    private final AntonioBanderasRepository antonioBanderasRepository;

    private final AntonioBanderasMapper antonioBanderasMapper;

    public AntonioBanderasQueryService(AntonioBanderasRepository antonioBanderasRepository, AntonioBanderasMapper antonioBanderasMapper) {
        this.antonioBanderasRepository = antonioBanderasRepository;
        this.antonioBanderasMapper = antonioBanderasMapper;
    }

    /**
     * Return a {@link Page} of {@link AntonioBanderasDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AntonioBanderasDTO> findByCriteria(AntonioBanderasCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AntonioBanderas> specification = createSpecification(criteria);
        return antonioBanderasRepository.findAll(specification, page).map(antonioBanderasMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AntonioBanderasCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AntonioBanderas> specification = createSpecification(criteria);
        return antonioBanderasRepository.count(specification);
    }

    /**
     * Function to convert {@link AntonioBanderasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AntonioBanderas> createSpecification(AntonioBanderasCriteria criteria) {
        Specification<AntonioBanderas> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AntonioBanderas_.id));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLevel(), AntonioBanderas_.level));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AntonioBanderas_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AntonioBanderas_.name));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), AntonioBanderas_.fullName));
            }
            if (criteria.getNativeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNativeName(), AntonioBanderas_.nativeName));
            }
            if (criteria.getOfficialCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficialCode(), AntonioBanderas_.officialCode));
            }
            if (criteria.getDivisionTerm() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDivisionTerm(), AntonioBanderas_.divisionTerm));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), AntonioBanderas_.isDeleted));
            }
            if (criteria.getCurrentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCurrentId(), root ->
                        root.join(AntonioBanderas_.current, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root ->
                        root.join(AntonioBanderas_.parent, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root ->
                        root.join(AntonioBanderas_.children, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
            if (criteria.getAntonioBanderasId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAntonioBanderasId(), root ->
                        root.join(AntonioBanderas_.antonioBanderas, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
        }
        return specification;
    }
}
