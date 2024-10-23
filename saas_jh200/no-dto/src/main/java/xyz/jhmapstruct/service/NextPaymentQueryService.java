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
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.repository.NextPaymentRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentCriteria;

/**
 * Service for executing complex queries for {@link NextPayment} entities in the database.
 * The main input is a {@link NextPaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPayment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentQueryService extends QueryService<NextPayment> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentQueryService.class);

    private final NextPaymentRepository nextPaymentRepository;

    public NextPaymentQueryService(NextPaymentRepository nextPaymentRepository) {
        this.nextPaymentRepository = nextPaymentRepository;
    }

    /**
     * Return a {@link Page} of {@link NextPayment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPayment> findByCriteria(NextPaymentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPayment> specification = createSpecification(criteria);
        return nextPaymentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPayment> specification = createSpecification(criteria);
        return nextPaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPayment> createSpecification(NextPaymentCriteria criteria) {
        Specification<NextPayment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPayment_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPayment_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPayment_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPayment_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextPayment_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
