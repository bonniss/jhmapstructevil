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
import xyz.jhmapstruct.domain.NextEmployeeViVi;
import xyz.jhmapstruct.repository.NextEmployeeViViRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeViViCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeViViDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeViViMapper;

/**
 * Service for executing complex queries for {@link NextEmployeeViVi} entities in the database.
 * The main input is a {@link NextEmployeeViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeViViQueryService extends QueryService<NextEmployeeViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViViQueryService.class);

    private final NextEmployeeViViRepository nextEmployeeViViRepository;

    private final NextEmployeeViViMapper nextEmployeeViViMapper;

    public NextEmployeeViViQueryService(
        NextEmployeeViViRepository nextEmployeeViViRepository,
        NextEmployeeViViMapper nextEmployeeViViMapper
    ) {
        this.nextEmployeeViViRepository = nextEmployeeViViRepository;
        this.nextEmployeeViViMapper = nextEmployeeViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeViViDTO> findByCriteria(NextEmployeeViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeViVi> specification = createSpecification(criteria);
        return nextEmployeeViViRepository.findAll(specification, page).map(nextEmployeeViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeViVi> specification = createSpecification(criteria);
        return nextEmployeeViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeViVi> createSpecification(NextEmployeeViViCriteria criteria) {
        Specification<NextEmployeeViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeViVi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeViVi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeViVi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeViVi_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeViVi_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeViVi_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}