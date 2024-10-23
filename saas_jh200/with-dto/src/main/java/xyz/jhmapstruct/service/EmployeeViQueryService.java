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
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.criteria.EmployeeViCriteria;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViMapper;

/**
 * Service for executing complex queries for {@link EmployeeVi} entities in the database.
 * The main input is a {@link EmployeeViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeViQueryService extends QueryService<EmployeeVi> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViQueryService.class);

    private final EmployeeViRepository employeeViRepository;

    private final EmployeeViMapper employeeViMapper;

    public EmployeeViQueryService(EmployeeViRepository employeeViRepository, EmployeeViMapper employeeViMapper) {
        this.employeeViRepository = employeeViRepository;
        this.employeeViMapper = employeeViMapper;
    }

    /**
     * Return a {@link Page} of {@link EmployeeViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeViDTO> findByCriteria(EmployeeViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeVi> specification = createSpecification(criteria);
        return employeeViRepository.findAll(specification, page).map(employeeViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeVi> specification = createSpecification(criteria);
        return employeeViRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeVi> createSpecification(EmployeeViCriteria criteria) {
        Specification<EmployeeVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeVi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeVi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeVi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeVi_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeVi_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeVi_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
