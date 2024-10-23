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
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.criteria.EmployeeMiCriteria;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;
import xyz.jhmapstruct.service.mapper.EmployeeMiMapper;

/**
 * Service for executing complex queries for {@link EmployeeMi} entities in the database.
 * The main input is a {@link EmployeeMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeMiQueryService extends QueryService<EmployeeMi> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiQueryService.class);

    private final EmployeeMiRepository employeeMiRepository;

    private final EmployeeMiMapper employeeMiMapper;

    public EmployeeMiQueryService(EmployeeMiRepository employeeMiRepository, EmployeeMiMapper employeeMiMapper) {
        this.employeeMiRepository = employeeMiRepository;
        this.employeeMiMapper = employeeMiMapper;
    }

    /**
     * Return a {@link Page} of {@link EmployeeMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeMiDTO> findByCriteria(EmployeeMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeMi> specification = createSpecification(criteria);
        return employeeMiRepository.findAll(specification, page).map(employeeMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeMi> specification = createSpecification(criteria);
        return employeeMiRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeMi> createSpecification(EmployeeMiCriteria criteria) {
        Specification<EmployeeMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeMi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeMi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeMi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeMi_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeMi_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeMi_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
