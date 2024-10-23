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
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.repository.NextCustomerViViRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerViViCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerViViDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerViViMapper;

/**
 * Service for executing complex queries for {@link NextCustomerViVi} entities in the database.
 * The main input is a {@link NextCustomerViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerViViQueryService extends QueryService<NextCustomerViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViViQueryService.class);

    private final NextCustomerViViRepository nextCustomerViViRepository;

    private final NextCustomerViViMapper nextCustomerViViMapper;

    public NextCustomerViViQueryService(
        NextCustomerViViRepository nextCustomerViViRepository,
        NextCustomerViViMapper nextCustomerViViMapper
    ) {
        this.nextCustomerViViRepository = nextCustomerViViRepository;
        this.nextCustomerViViMapper = nextCustomerViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerViViDTO> findByCriteria(NextCustomerViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerViVi> specification = createSpecification(criteria);
        return nextCustomerViViRepository.findAll(specification, page).map(nextCustomerViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerViVi> specification = createSpecification(criteria);
        return nextCustomerViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerViVi> createSpecification(NextCustomerViViCriteria criteria) {
        Specification<NextCustomerViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerViVi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerViVi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerViVi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerViVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerViVi_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root ->
                        root.join(NextCustomerViVi_.orders, JoinType.LEFT).get(NextOrderViVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
