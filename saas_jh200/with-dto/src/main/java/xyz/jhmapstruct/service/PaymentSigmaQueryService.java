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
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.repository.PaymentSigmaRepository;
import xyz.jhmapstruct.service.criteria.PaymentSigmaCriteria;
import xyz.jhmapstruct.service.dto.PaymentSigmaDTO;
import xyz.jhmapstruct.service.mapper.PaymentSigmaMapper;

/**
 * Service for executing complex queries for {@link PaymentSigma} entities in the database.
 * The main input is a {@link PaymentSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentSigmaQueryService extends QueryService<PaymentSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSigmaQueryService.class);

    private final PaymentSigmaRepository paymentSigmaRepository;

    private final PaymentSigmaMapper paymentSigmaMapper;

    public PaymentSigmaQueryService(PaymentSigmaRepository paymentSigmaRepository, PaymentSigmaMapper paymentSigmaMapper) {
        this.paymentSigmaRepository = paymentSigmaRepository;
        this.paymentSigmaMapper = paymentSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link PaymentSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentSigmaDTO> findByCriteria(PaymentSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentSigma> specification = createSpecification(criteria);
        return paymentSigmaRepository.findAll(specification, page).map(paymentSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentSigma> specification = createSpecification(criteria);
        return paymentSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentSigma> createSpecification(PaymentSigmaCriteria criteria) {
        Specification<PaymentSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentSigma_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentSigma_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentSigma_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentSigma_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
