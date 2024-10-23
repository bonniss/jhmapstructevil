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
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.repository.NextPaymentBetaRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentBetaCriteria;

/**
 * Service for executing complex queries for {@link NextPaymentBeta} entities in the database.
 * The main input is a {@link NextPaymentBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentBetaQueryService extends QueryService<NextPaymentBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentBetaQueryService.class);

    private final NextPaymentBetaRepository nextPaymentBetaRepository;

    public NextPaymentBetaQueryService(NextPaymentBetaRepository nextPaymentBetaRepository) {
        this.nextPaymentBetaRepository = nextPaymentBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentBeta> findByCriteria(NextPaymentBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentBeta> specification = createSpecification(criteria);
        return nextPaymentBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentBeta> specification = createSpecification(criteria);
        return nextPaymentBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentBeta> createSpecification(NextPaymentBetaCriteria criteria) {
        Specification<NextPaymentBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentBeta_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentBeta_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentBeta_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentBeta_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
