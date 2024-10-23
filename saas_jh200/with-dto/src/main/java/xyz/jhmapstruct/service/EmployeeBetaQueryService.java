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
import xyz.jhmapstruct.domain.EmployeeBeta;
import xyz.jhmapstruct.repository.EmployeeBetaRepository;
import xyz.jhmapstruct.service.criteria.EmployeeBetaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeBetaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeBetaMapper;

/**
 * Service for executing complex queries for {@link EmployeeBeta} entities in the database.
 * The main input is a {@link EmployeeBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EmployeeBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeBetaQueryService extends QueryService<EmployeeBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBetaQueryService.class);

    private final EmployeeBetaRepository employeeBetaRepository;

    private final EmployeeBetaMapper employeeBetaMapper;

    public EmployeeBetaQueryService(EmployeeBetaRepository employeeBetaRepository, EmployeeBetaMapper employeeBetaMapper) {
        this.employeeBetaRepository = employeeBetaRepository;
        this.employeeBetaMapper = employeeBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link EmployeeBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeBetaDTO> findByCriteria(EmployeeBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EmployeeBeta> specification = createSpecification(criteria);
        return employeeBetaRepository.findAll(specification, page).map(employeeBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EmployeeBeta> specification = createSpecification(criteria);
        return employeeBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EmployeeBeta> createSpecification(EmployeeBetaCriteria criteria) {
        Specification<EmployeeBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EmployeeBeta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), EmployeeBeta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), EmployeeBeta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), EmployeeBeta_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), EmployeeBeta_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), EmployeeBeta_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(EmployeeBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
