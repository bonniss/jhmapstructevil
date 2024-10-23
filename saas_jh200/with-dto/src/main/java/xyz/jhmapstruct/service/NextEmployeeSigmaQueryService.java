package xyz.jhmapstruct.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import xyz.jhmapstruct.domain.*; // for static metamodels
import xyz.jhmapstruct.domain.NextEmployeeSigma;
import xyz.jhmapstruct.repository.NextEmployeeSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeSigmaMapper;

/**
 * Service for executing complex queries for {@link NextEmployeeSigma} entities in the database.
 * The main input is a {@link NextEmployeeSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeSigmaQueryService extends QueryService<NextEmployeeSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeSigmaQueryService.class);

    private final NextEmployeeSigmaRepository nextEmployeeSigmaRepository;

    private final NextEmployeeSigmaMapper nextEmployeeSigmaMapper;

    public NextEmployeeSigmaQueryService(
        NextEmployeeSigmaRepository nextEmployeeSigmaRepository,
        NextEmployeeSigmaMapper nextEmployeeSigmaMapper
    ) {
        this.nextEmployeeSigmaRepository = nextEmployeeSigmaRepository;
        this.nextEmployeeSigmaMapper = nextEmployeeSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeSigmaDTO> findByCriteria(NextEmployeeSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeSigma> specification = createSpecification(criteria);
        return nextEmployeeSigmaRepository.findAll(specification, page).map(nextEmployeeSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeSigma> specification = createSpecification(criteria);
        return nextEmployeeSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeSigma> createSpecification(NextEmployeeSigmaCriteria criteria) {
        Specification<NextEmployeeSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeSigma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeSigma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeSigma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeSigma_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeSigma_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeSigma_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
