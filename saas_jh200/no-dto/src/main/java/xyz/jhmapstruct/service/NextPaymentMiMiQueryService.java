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
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.repository.NextPaymentMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentMiMiCriteria;

/**
 * Service for executing complex queries for {@link NextPaymentMiMi} entities in the database.
 * The main input is a {@link NextPaymentMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentMiMiQueryService extends QueryService<NextPaymentMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiMiQueryService.class);

    private final NextPaymentMiMiRepository nextPaymentMiMiRepository;

    public NextPaymentMiMiQueryService(NextPaymentMiMiRepository nextPaymentMiMiRepository) {
        this.nextPaymentMiMiRepository = nextPaymentMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentMiMi> findByCriteria(NextPaymentMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentMiMi> specification = createSpecification(criteria);
        return nextPaymentMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentMiMi> specification = createSpecification(criteria);
        return nextPaymentMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentMiMi> createSpecification(NextPaymentMiMiCriteria criteria) {
        Specification<NextPaymentMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentMiMi_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentMiMi_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentMiMi_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentMiMi_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
