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
import xyz.jhmapstruct.domain.CustomerGamma;
import xyz.jhmapstruct.repository.CustomerGammaRepository;
import xyz.jhmapstruct.service.criteria.CustomerGammaCriteria;
import xyz.jhmapstruct.service.dto.CustomerGammaDTO;
import xyz.jhmapstruct.service.mapper.CustomerGammaMapper;

/**
 * Service for executing complex queries for {@link CustomerGamma} entities in the database.
 * The main input is a {@link CustomerGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerGammaQueryService extends QueryService<CustomerGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerGammaQueryService.class);

    private final CustomerGammaRepository customerGammaRepository;

    private final CustomerGammaMapper customerGammaMapper;

    public CustomerGammaQueryService(CustomerGammaRepository customerGammaRepository, CustomerGammaMapper customerGammaMapper) {
        this.customerGammaRepository = customerGammaRepository;
        this.customerGammaMapper = customerGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerGammaDTO> findByCriteria(CustomerGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerGamma> specification = createSpecification(criteria);
        return customerGammaRepository.findAll(specification, page).map(customerGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerGamma> specification = createSpecification(criteria);
        return customerGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerGamma> createSpecification(CustomerGammaCriteria criteria) {
        Specification<CustomerGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerGamma_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerGamma_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerGamma_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerGamma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerGamma_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerGamma_.orders, JoinType.LEFT).get(OrderGamma_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
