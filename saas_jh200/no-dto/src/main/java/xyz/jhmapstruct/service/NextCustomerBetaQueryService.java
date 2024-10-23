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
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.repository.NextCustomerBetaRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerBetaCriteria;

/**
 * Service for executing complex queries for {@link NextCustomerBeta} entities in the database.
 * The main input is a {@link NextCustomerBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerBetaQueryService extends QueryService<NextCustomerBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerBetaQueryService.class);

    private final NextCustomerBetaRepository nextCustomerBetaRepository;

    public NextCustomerBetaQueryService(NextCustomerBetaRepository nextCustomerBetaRepository) {
        this.nextCustomerBetaRepository = nextCustomerBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerBeta> findByCriteria(NextCustomerBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerBeta> specification = createSpecification(criteria);
        return nextCustomerBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerBeta> specification = createSpecification(criteria);
        return nextCustomerBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerBeta> createSpecification(NextCustomerBetaCriteria criteria) {
        Specification<NextCustomerBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerBeta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerBeta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerBeta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerBeta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerBeta_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root ->
                        root.join(NextCustomerBeta_.orders, JoinType.LEFT).get(NextOrderBeta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
