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
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.repository.PaymentBetaRepository;
import xyz.jhmapstruct.service.criteria.PaymentBetaCriteria;
import xyz.jhmapstruct.service.dto.PaymentBetaDTO;
import xyz.jhmapstruct.service.mapper.PaymentBetaMapper;

/**
 * Service for executing complex queries for {@link PaymentBeta} entities in the database.
 * The main input is a {@link PaymentBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentBetaQueryService extends QueryService<PaymentBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentBetaQueryService.class);

    private final PaymentBetaRepository paymentBetaRepository;

    private final PaymentBetaMapper paymentBetaMapper;

    public PaymentBetaQueryService(PaymentBetaRepository paymentBetaRepository, PaymentBetaMapper paymentBetaMapper) {
        this.paymentBetaRepository = paymentBetaRepository;
        this.paymentBetaMapper = paymentBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link PaymentBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentBetaDTO> findByCriteria(PaymentBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentBeta> specification = createSpecification(criteria);
        return paymentBetaRepository.findAll(specification, page).map(paymentBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentBeta> specification = createSpecification(criteria);
        return paymentBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentBeta> createSpecification(PaymentBetaCriteria criteria) {
        Specification<PaymentBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentBeta_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentBeta_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentBeta_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentBeta_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
