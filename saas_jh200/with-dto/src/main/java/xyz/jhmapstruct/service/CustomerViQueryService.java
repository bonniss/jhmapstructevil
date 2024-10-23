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
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.criteria.CustomerViCriteria;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViMapper;

/**
 * Service for executing complex queries for {@link CustomerVi} entities in the database.
 * The main input is a {@link CustomerViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerViQueryService extends QueryService<CustomerVi> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViQueryService.class);

    private final CustomerViRepository customerViRepository;

    private final CustomerViMapper customerViMapper;

    public CustomerViQueryService(CustomerViRepository customerViRepository, CustomerViMapper customerViMapper) {
        this.customerViRepository = customerViRepository;
        this.customerViMapper = customerViMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerViDTO> findByCriteria(CustomerViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerVi> specification = createSpecification(criteria);
        return customerViRepository.findAll(specification, page).map(customerViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerVi> specification = createSpecification(criteria);
        return customerViRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerVi> createSpecification(CustomerViCriteria criteria) {
        Specification<CustomerVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerVi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerVi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerVi_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerVi_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerVi_.orders, JoinType.LEFT).get(OrderVi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
