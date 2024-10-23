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
import xyz.jhmapstruct.domain.CustomerTheta;
import xyz.jhmapstruct.repository.CustomerThetaRepository;
import xyz.jhmapstruct.service.criteria.CustomerThetaCriteria;

/**
 * Service for executing complex queries for {@link CustomerTheta} entities in the database.
 * The main input is a {@link CustomerThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CustomerTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerThetaQueryService extends QueryService<CustomerTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerThetaQueryService.class);

    private final CustomerThetaRepository customerThetaRepository;

    public CustomerThetaQueryService(CustomerThetaRepository customerThetaRepository) {
        this.customerThetaRepository = customerThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link CustomerTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerTheta> findByCriteria(CustomerThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerTheta> specification = createSpecification(criteria);
        return customerThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CustomerTheta> specification = createSpecification(criteria);
        return customerThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerTheta> createSpecification(CustomerThetaCriteria criteria) {
        Specification<CustomerTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerTheta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), CustomerTheta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), CustomerTheta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CustomerTheta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), CustomerTheta_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root -> root.join(CustomerTheta_.orders, JoinType.LEFT).get(OrderTheta_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(CustomerTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
