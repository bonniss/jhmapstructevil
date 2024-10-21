package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.HexChar;
import ai.realworld.repository.HexCharRepository;
import ai.realworld.service.criteria.HexCharCriteria;
import ai.realworld.service.dto.HexCharDTO;
import ai.realworld.service.mapper.HexCharMapper;
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
 * Service for executing complex queries for {@link HexChar} entities in the database.
 * The main input is a {@link HexCharCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HexCharDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HexCharQueryService extends QueryService<HexChar> {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharQueryService.class);

    private final HexCharRepository hexCharRepository;

    private final HexCharMapper hexCharMapper;

    public HexCharQueryService(HexCharRepository hexCharRepository, HexCharMapper hexCharMapper) {
        this.hexCharRepository = hexCharRepository;
        this.hexCharMapper = hexCharMapper;
    }

    /**
     * Return a {@link Page} of {@link HexCharDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HexCharDTO> findByCriteria(HexCharCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HexChar> specification = createSpecification(criteria);
        return hexCharRepository.findAll(specification, page).map(hexCharMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HexCharCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HexChar> specification = createSpecification(criteria);
        return hexCharRepository.count(specification);
    }

    /**
     * Function to convert {@link HexCharCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HexChar> createSpecification(HexCharCriteria criteria) {
        Specification<HexChar> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HexChar_.id));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), HexChar_.dob));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), HexChar_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), HexChar_.phone));
            }
            if (criteria.getBioHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBioHeitiga(), HexChar_.bioHeitiga));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), HexChar_.isEnabled));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root -> root.join(HexChar_.internalUser, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoleId(), root -> root.join(HexChar_.role, JoinType.LEFT).get(HashRoss_.id))
                );
            }
        }
        return specification;
    }
}
