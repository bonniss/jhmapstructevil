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
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.repository.PaymentGammaRepository;
import xyz.jhmapstruct.service.criteria.PaymentGammaCriteria;

/**
 * Service for executing complex queries for {@link PaymentGamma} entities in the database.
 * The main input is a {@link PaymentGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentGammaQueryService extends QueryService<PaymentGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGammaQueryService.class);

    private final PaymentGammaRepository paymentGammaRepository;

    public PaymentGammaQueryService(PaymentGammaRepository paymentGammaRepository) {
        this.paymentGammaRepository = paymentGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link PaymentGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentGamma> findByCriteria(PaymentGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentGamma> specification = createSpecification(criteria);
        return paymentGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentGamma> specification = createSpecification(criteria);
        return paymentGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentGamma> createSpecification(PaymentGammaCriteria criteria) {
        Specification<PaymentGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentGamma_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentGamma_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentGamma_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentGamma_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
