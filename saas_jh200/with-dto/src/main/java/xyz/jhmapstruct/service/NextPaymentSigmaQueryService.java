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
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.repository.NextPaymentSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentSigmaMapper;

/**
 * Service for executing complex queries for {@link NextPaymentSigma} entities in the database.
 * The main input is a {@link NextPaymentSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentSigmaQueryService extends QueryService<NextPaymentSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentSigmaQueryService.class);

    private final NextPaymentSigmaRepository nextPaymentSigmaRepository;

    private final NextPaymentSigmaMapper nextPaymentSigmaMapper;

    public NextPaymentSigmaQueryService(
        NextPaymentSigmaRepository nextPaymentSigmaRepository,
        NextPaymentSigmaMapper nextPaymentSigmaMapper
    ) {
        this.nextPaymentSigmaRepository = nextPaymentSigmaRepository;
        this.nextPaymentSigmaMapper = nextPaymentSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentSigmaDTO> findByCriteria(NextPaymentSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentSigma> specification = createSpecification(criteria);
        return nextPaymentSigmaRepository.findAll(specification, page).map(nextPaymentSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentSigma> specification = createSpecification(criteria);
        return nextPaymentSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentSigma> createSpecification(NextPaymentSigmaCriteria criteria) {
        Specification<NextPaymentSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentSigma_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentSigma_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentSigma_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentSigma_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
