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
import xyz.jhmapstruct.domain.CustomerBeta;
import xyz.jhmapstruct.repository.CustomerBetaRepository;
import xyz.jhmapstruct.service.criteria.CustomerBetaCriteria;

/**
 * Service for executing complex queries for {@link CustomerBeta} entities in the database.
 * The main input is a {@link CustomerBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerBetaQueryService extends QueryService<CustomerBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerBetaQueryService.class);

    private final CustomerBetaRepository customerBetaRepository;

    public CustomerBetaQueryService(CustomerBetaRepository customerBetaRepository) {
        this.customerBetaRepository = customerBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link CustomerBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerBeta> findByCriteria(CustomerBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerBeta> specification = createSpecification(criteria);
        return customerBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerBeta> specification = createSpecification(criteria);
        return customerBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerBeta> createSpecification(CustomerBetaCriteria criteria) {
        Specification<CustomerBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerBeta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerBeta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerBeta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerBeta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerBeta_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerBeta_.orders, JoinType.LEFT).get(OrderBeta_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
