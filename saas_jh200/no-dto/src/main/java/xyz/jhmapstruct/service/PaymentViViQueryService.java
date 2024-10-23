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
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.repository.PaymentViViRepository;
import xyz.jhmapstruct.service.criteria.PaymentViViCriteria;

/**
 * Service for executing complex queries for {@link PaymentViVi} entities in the database.
 * The main input is a {@link PaymentViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentViViQueryService extends QueryService<PaymentViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViViQueryService.class);

    private final PaymentViViRepository paymentViViRepository;

    public PaymentViViQueryService(PaymentViViRepository paymentViViRepository) {
        this.paymentViViRepository = paymentViViRepository;
    }

    /**
     * Return a {@link Page} of {@link PaymentViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentViVi> findByCriteria(PaymentViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentViVi> specification = createSpecification(criteria);
        return paymentViViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentViVi> specification = createSpecification(criteria);
        return paymentViViRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentViVi> createSpecification(PaymentViViCriteria criteria) {
        Specification<PaymentViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentViVi_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentViVi_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentViVi_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentViVi_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
