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
import xyz.jhmapstruct.domain.NextCustomerMi;
import xyz.jhmapstruct.repository.NextCustomerMiRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerMiCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerMiDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMiMapper;

/**
 * Service for executing complex queries for {@link NextCustomerMi} entities in the database.
 * The main input is a {@link NextCustomerMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerMiQueryService extends QueryService<NextCustomerMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerMiQueryService.class);

    private final NextCustomerMiRepository nextCustomerMiRepository;

    private final NextCustomerMiMapper nextCustomerMiMapper;

    public NextCustomerMiQueryService(NextCustomerMiRepository nextCustomerMiRepository, NextCustomerMiMapper nextCustomerMiMapper) {
        this.nextCustomerMiRepository = nextCustomerMiRepository;
        this.nextCustomerMiMapper = nextCustomerMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerMiDTO> findByCriteria(NextCustomerMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerMi> specification = createSpecification(criteria);
        return nextCustomerMiRepository.findAll(specification, page).map(nextCustomerMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerMi> specification = createSpecification(criteria);
        return nextCustomerMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerMi> createSpecification(NextCustomerMiCriteria criteria) {
        Specification<NextCustomerMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerMi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerMi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerMi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerMi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerMi_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(NextCustomerMi_.orders, JoinType.LEFT).get(OrderMi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
