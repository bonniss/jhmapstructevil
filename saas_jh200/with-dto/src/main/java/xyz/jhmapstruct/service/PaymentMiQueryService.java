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
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.criteria.PaymentMiCriteria;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
import xyz.jhmapstruct.service.mapper.PaymentMiMapper;

/**
 * Service for executing complex queries for {@link PaymentMi} entities in the database.
 * The main input is a {@link PaymentMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMiQueryService extends QueryService<PaymentMi> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiQueryService.class);

    private final PaymentMiRepository paymentMiRepository;

    private final PaymentMiMapper paymentMiMapper;

    public PaymentMiQueryService(PaymentMiRepository paymentMiRepository, PaymentMiMapper paymentMiMapper) {
        this.paymentMiRepository = paymentMiRepository;
        this.paymentMiMapper = paymentMiMapper;
    }

    /**
     * Return a {@link Page} of {@link PaymentMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentMiDTO> findByCriteria(PaymentMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentMi> specification = createSpecification(criteria);
        return paymentMiRepository.findAll(specification, page).map(paymentMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentMi> specification = createSpecification(criteria);
        return paymentMiRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentMi> createSpecification(PaymentMiCriteria criteria) {
        Specification<PaymentMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentMi_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentMi_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentMi_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentMi_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
