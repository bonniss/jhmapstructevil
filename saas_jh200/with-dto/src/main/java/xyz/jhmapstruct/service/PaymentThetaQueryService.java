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
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.repository.PaymentThetaRepository;
import xyz.jhmapstruct.service.criteria.PaymentThetaCriteria;
import xyz.jhmapstruct.service.dto.PaymentThetaDTO;
import xyz.jhmapstruct.service.mapper.PaymentThetaMapper;

/**
 * Service for executing complex queries for {@link PaymentTheta} entities in the database.
 * The main input is a {@link PaymentThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PaymentThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentThetaQueryService extends QueryService<PaymentTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentThetaQueryService.class);

    private final PaymentThetaRepository paymentThetaRepository;

    private final PaymentThetaMapper paymentThetaMapper;

    public PaymentThetaQueryService(PaymentThetaRepository paymentThetaRepository, PaymentThetaMapper paymentThetaMapper) {
        this.paymentThetaRepository = paymentThetaRepository;
        this.paymentThetaMapper = paymentThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link PaymentThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentThetaDTO> findByCriteria(PaymentThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentTheta> specification = createSpecification(criteria);
        return paymentThetaRepository.findAll(specification, page).map(paymentThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PaymentTheta> specification = createSpecification(criteria);
        return paymentThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentTheta> createSpecification(PaymentThetaCriteria criteria) {
        Specification<PaymentTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentTheta_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), PaymentTheta_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentTheta_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), PaymentTheta_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(PaymentTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
