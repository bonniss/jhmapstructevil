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
import xyz.jhmapstruct.domain.EmployeeGamma;
import xyz.jhmapstruct.repository.EmployeeGammaRepository;
import xyz.jhmapstruct.service.criteria.EmployeeGammaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeGammaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeGammaMapper;

/**
 * Service for executing complex queries for {@link EmployeeGamma} entities in the database.
 * The main input is a {@link EmployeeGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeGammaQueryService extends QueryService<EmployeeGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeGammaQueryService.class);

    private final EmployeeGammaRepository employeeGammaRepository;

    private final EmployeeGammaMapper employeeGammaMapper;

    public EmployeeGammaQueryService(EmployeeGammaRepository employeeGammaRepository, EmployeeGammaMapper employeeGammaMapper) {
        this.employeeGammaRepository = employeeGammaRepository;
        this.employeeGammaMapper = employeeGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link EmployeeGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeGammaDTO> findByCriteria(EmployeeGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeGamma> specification = createSpecification(criteria);
        return employeeGammaRepository.findAll(specification, page).map(employeeGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeGamma> specification = createSpecification(criteria);
        return employeeGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeGamma> createSpecification(EmployeeGammaCriteria criteria) {
        Specification<EmployeeGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeGamma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeGamma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeGamma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeGamma_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeGamma_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeGamma_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
