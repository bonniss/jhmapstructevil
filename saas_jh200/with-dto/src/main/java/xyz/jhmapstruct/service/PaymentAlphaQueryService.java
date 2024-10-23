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
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.repository.PaymentAlphaRepository;
import xyz.jhmapstruct.service.criteria.PaymentAlphaCriteria;
import xyz.jhmapstruct.service.dto.PaymentAlphaDTO;
import xyz.jhmapstruct.service.mapper.PaymentAlphaMapper;

/**
 * Service for executing complex queries for {@link PaymentAlpha} entities in the database.
 * The main input is a {@link PaymentAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentAlphaQueryService extends QueryService<PaymentAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAlphaQueryService.class);

    private final PaymentAlphaRepository paymentAlphaRepository;

    private final PaymentAlphaMapper paymentAlphaMapper;

    public PaymentAlphaQueryService(PaymentAlphaRepository paymentAlphaRepository, PaymentAlphaMapper paymentAlphaMapper) {
        this.paymentAlphaRepository = paymentAlphaRepository;
        this.paymentAlphaMapper = paymentAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link PaymentAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentAlphaDTO> findByCriteria(PaymentAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentAlpha> specification = createSpecification(criteria);
        return paymentAlphaRepository.findAll(specification, page).map(paymentAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentAlpha> specification = createSpecification(criteria);
        return paymentAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentAlpha> createSpecification(PaymentAlphaCriteria criteria) {
        Specification<PaymentAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentAlpha_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentAlpha_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentAlpha_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentAlpha_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
