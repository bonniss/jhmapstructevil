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
import xyz.jhmapstruct.domain.NextEmployeeTheta;
import xyz.jhmapstruct.repository.NextEmployeeThetaRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeThetaCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeThetaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeThetaMapper;

/**
 * Service for executing complex queries for {@link NextEmployeeTheta} entities in the database.
 * The main input is a {@link NextEmployeeThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeThetaQueryService extends QueryService<NextEmployeeTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeThetaQueryService.class);

    private final NextEmployeeThetaRepository nextEmployeeThetaRepository;

    private final NextEmployeeThetaMapper nextEmployeeThetaMapper;

    public NextEmployeeThetaQueryService(
        NextEmployeeThetaRepository nextEmployeeThetaRepository,
        NextEmployeeThetaMapper nextEmployeeThetaMapper
    ) {
        this.nextEmployeeThetaRepository = nextEmployeeThetaRepository;
        this.nextEmployeeThetaMapper = nextEmployeeThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeThetaDTO> findByCriteria(NextEmployeeThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeTheta> specification = createSpecification(criteria);
        return nextEmployeeThetaRepository.findAll(specification, page).map(nextEmployeeThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeTheta> specification = createSpecification(criteria);
        return nextEmployeeThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeTheta> createSpecification(NextEmployeeThetaCriteria criteria) {
        Specification<NextEmployeeTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeTheta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeTheta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeTheta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeTheta_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeTheta_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeTheta_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
