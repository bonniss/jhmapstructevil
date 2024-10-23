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
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.repository.PaymentViRepository;
import xyz.jhmapstruct.service.criteria.PaymentViCriteria;

/**
 * Service for executing complex queries for {@link PaymentVi} entities in the database.
 * The main input is a {@link PaymentViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentViQueryService extends QueryService<PaymentVi> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViQueryService.class);

    private final PaymentViRepository paymentViRepository;

    public PaymentViQueryService(PaymentViRepository paymentViRepository) {
        this.paymentViRepository = paymentViRepository;
    }

    /**
     * Return a {@link Page} of {@link PaymentVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentVi> findByCriteria(PaymentViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentVi> specification = createSpecification(criteria);
        return paymentViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentVi> specification = createSpecification(criteria);
        return paymentViRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentVi> createSpecification(PaymentViCriteria criteria) {
        Specification<PaymentVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentVi_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentVi_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentVi_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentVi_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
