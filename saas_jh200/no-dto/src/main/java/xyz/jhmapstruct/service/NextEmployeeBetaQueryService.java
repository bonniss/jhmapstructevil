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
import xyz.jhmapstruct.domain.NextEmployeeBeta;
import xyz.jhmapstruct.repository.NextEmployeeBetaRepository;
import xyz.jhmapstruct.service.criteria.NextEmployeeBetaCriteria;

/**
 * Service for executing complex queries for {@link NextEmployeeBeta} entities in the database.
 * The main input is a {@link NextEmployeeBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextEmployeeBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextEmployeeBetaQueryService extends QueryService<NextEmployeeBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeBetaQueryService.class);

    private final NextEmployeeBetaRepository nextEmployeeBetaRepository;

    public NextEmployeeBetaQueryService(NextEmployeeBetaRepository nextEmployeeBetaRepository) {
        this.nextEmployeeBetaRepository = nextEmployeeBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextEmployeeBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextEmployeeBeta> findByCriteria(NextEmployeeBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextEmployeeBeta> specification = createSpecification(criteria);
        return nextEmployeeBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextEmployeeBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextEmployeeBeta> specification = createSpecification(criteria);
        return nextEmployeeBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextEmployeeBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextEmployeeBeta> createSpecification(NextEmployeeBetaCriteria criteria) {
        Specification<NextEmployeeBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextEmployeeBeta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextEmployeeBeta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextEmployeeBeta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextEmployeeBeta_.email));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), NextEmployeeBeta_.hireDate));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), NextEmployeeBeta_.position));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextEmployeeBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
