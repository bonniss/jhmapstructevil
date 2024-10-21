package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.HexCharVi;
import ai.realworld.repository.HexCharViRepository;
import ai.realworld.service.criteria.HexCharViCriteria;
import ai.realworld.service.dto.HexCharViDTO;
import ai.realworld.service.mapper.HexCharViMapper;
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
 * Service for executing complex queries for {@link HexCharVi} entities in the database.
 * The main input is a {@link HexCharViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HexCharViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HexCharViQueryService extends QueryService<HexCharVi> {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharViQueryService.class);

    private final HexCharViRepository hexCharViRepository;

    private final HexCharViMapper hexCharViMapper;

    public HexCharViQueryService(HexCharViRepository hexCharViRepository, HexCharViMapper hexCharViMapper) {
        this.hexCharViRepository = hexCharViRepository;
        this.hexCharViMapper = hexCharViMapper;
    }

    /**
     * Return a {@link Page} of {@link HexCharViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HexCharViDTO> findByCriteria(HexCharViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HexCharVi> specification = createSpecification(criteria);
        return hexCharViRepository.findAll(specification, page).map(hexCharViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HexCharViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HexCharVi> specification = createSpecification(criteria);
        return hexCharViRepository.count(specification);
    }

    /**
     * Function to convert {@link HexCharViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HexCharVi> createSpecification(HexCharViCriteria criteria) {
        Specification<HexCharVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HexCharVi_.id));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), HexCharVi_.dob));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), HexCharVi_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), HexCharVi_.phone));
            }
            if (criteria.getBioHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBioHeitiga(), HexCharVi_.bioHeitiga));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), HexCharVi_.isEnabled));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root -> root.join(HexCharVi_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoleId(), root -> root.join(HexCharVi_.role, JoinType.LEFT).get(HashRossVi_.id))
                );
            }
        }
        return specification;
    }
}
