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
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.criteria.CustomerMiMiCriteria;

/**
 * Service for executing complex queries for {@link CustomerMiMi} entities in the database.
 * The main input is a {@link CustomerMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerMiMiQueryService extends QueryService<CustomerMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiMiQueryService.class);

    private final CustomerMiMiRepository customerMiMiRepository;

    public CustomerMiMiQueryService(CustomerMiMiRepository customerMiMiRepository) {
        this.customerMiMiRepository = customerMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link CustomerMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerMiMi> findByCriteria(CustomerMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerMiMi> specification = createSpecification(criteria);
        return customerMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerMiMi> specification = createSpecification(criteria);
        return customerMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerMiMi> createSpecification(CustomerMiMiCriteria criteria) {
        Specification<CustomerMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerMiMi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerMiMi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerMiMi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerMiMi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerMiMi_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerMiMi_.orders, JoinType.LEFT).get(OrderMiMi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
