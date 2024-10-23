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
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.repository.EmployeeViViRepository;
import xyz.jhmapstruct.service.criteria.EmployeeViViCriteria;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViViMapper;

/**
 * Service for executing complex queries for {@link EmployeeViVi} entities in the database.
 * The main input is a {@link EmployeeViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeViViQueryService extends QueryService<EmployeeViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViViQueryService.class);

    private final EmployeeViViRepository employeeViViRepository;

    private final EmployeeViViMapper employeeViViMapper;

    public EmployeeViViQueryService(EmployeeViViRepository employeeViViRepository, EmployeeViViMapper employeeViViMapper) {
        this.employeeViViRepository = employeeViViRepository;
        this.employeeViViMapper = employeeViViMapper;
    }

    /**
     * Return a {@link Page} of {@link EmployeeViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeViViDTO> findByCriteria(EmployeeViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeViVi> specification = createSpecification(criteria);
        return employeeViViRepository.findAll(specification, page).map(employeeViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeViVi> specification = createSpecification(criteria);
        return employeeViViRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeViVi> createSpecification(EmployeeViViCriteria criteria) {
        Specification<EmployeeViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeViVi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeViVi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeViVi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeViVi_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeViVi_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeViVi_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
