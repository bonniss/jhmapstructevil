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
import xyz.jhmapstruct.domain.EmployeeTheta;
import xyz.jhmapstruct.repository.EmployeeThetaRepository;
import xyz.jhmapstruct.service.criteria.EmployeeThetaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeThetaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeThetaMapper;

/**
 * Service for executing complex queries for {@link EmployeeTheta} entities in the database.
 * The main input is a {@link EmployeeThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeThetaQueryService extends QueryService<EmployeeTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeThetaQueryService.class);

    private final EmployeeThetaRepository employeeThetaRepository;

    private final EmployeeThetaMapper employeeThetaMapper;

    public EmployeeThetaQueryService(EmployeeThetaRepository employeeThetaRepository, EmployeeThetaMapper employeeThetaMapper) {
        this.employeeThetaRepository = employeeThetaRepository;
        this.employeeThetaMapper = employeeThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link EmployeeThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeThetaDTO> findByCriteria(EmployeeThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeTheta> specification = createSpecification(criteria);
        return employeeThetaRepository.findAll(specification, page).map(employeeThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeTheta> specification = createSpecification(criteria);
        return employeeThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeTheta> createSpecification(EmployeeThetaCriteria criteria) {
        Specification<EmployeeTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeTheta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeTheta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeTheta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeTheta_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeTheta_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeTheta_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
