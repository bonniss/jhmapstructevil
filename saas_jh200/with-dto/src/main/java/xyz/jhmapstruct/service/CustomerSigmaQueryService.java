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
import xyz.jhmapstruct.domain.CustomerSigma;
import xyz.jhmapstruct.repository.CustomerSigmaRepository;
import xyz.jhmapstruct.service.criteria.CustomerSigmaCriteria;
import xyz.jhmapstruct.service.dto.CustomerSigmaDTO;
import xyz.jhmapstruct.service.mapper.CustomerSigmaMapper;

/**
 * Service for executing complex queries for {@link CustomerSigma} entities in the database.
 * The main input is a {@link CustomerSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerSigmaQueryService extends QueryService<CustomerSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerSigmaQueryService.class);

    private final CustomerSigmaRepository customerSigmaRepository;

    private final CustomerSigmaMapper customerSigmaMapper;

    public CustomerSigmaQueryService(CustomerSigmaRepository customerSigmaRepository, CustomerSigmaMapper customerSigmaMapper) {
        this.customerSigmaRepository = customerSigmaRepository;
        this.customerSigmaMapper = customerSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerSigmaDTO> findByCriteria(CustomerSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerSigma> specification = createSpecification(criteria);
        return customerSigmaRepository.findAll(specification, page).map(customerSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerSigma> specification = createSpecification(criteria);
        return customerSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerSigma> createSpecification(CustomerSigmaCriteria criteria) {
        Specification<CustomerSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerSigma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerSigma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerSigma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerSigma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerSigma_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerSigma_.orders, JoinType.LEFT).get(OrderSigma_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
