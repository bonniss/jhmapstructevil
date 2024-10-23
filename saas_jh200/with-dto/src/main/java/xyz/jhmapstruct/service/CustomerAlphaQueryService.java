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
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.repository.CustomerAlphaRepository;
import xyz.jhmapstruct.service.criteria.CustomerAlphaCriteria;
import xyz.jhmapstruct.service.dto.CustomerAlphaDTO;
import xyz.jhmapstruct.service.mapper.CustomerAlphaMapper;

/**
 * Service for executing complex queries for {@link CustomerAlpha} entities in the database.
 * The main input is a {@link CustomerAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerAlphaQueryService extends QueryService<CustomerAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAlphaQueryService.class);

    private final CustomerAlphaRepository customerAlphaRepository;

    private final CustomerAlphaMapper customerAlphaMapper;

    public CustomerAlphaQueryService(CustomerAlphaRepository customerAlphaRepository, CustomerAlphaMapper customerAlphaMapper) {
        this.customerAlphaRepository = customerAlphaRepository;
        this.customerAlphaMapper = customerAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link CustomerAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerAlphaDTO> findByCriteria(CustomerAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerAlpha> specification = createSpecification(criteria);
        return customerAlphaRepository.findAll(specification, page).map(customerAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerAlpha> specification = createSpecification(criteria);
        return customerAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerAlpha> createSpecification(CustomerAlphaCriteria criteria) {
        Specification<CustomerAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerAlpha_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerAlpha_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerAlpha_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerAlpha_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerAlpha_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerAlpha_.orders, JoinType.LEFT).get(OrderAlpha_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
