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
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.repository.NextPaymentViRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentViCriteria;

/**
 * Service for executing complex queries for {@link NextPaymentVi} entities in the database.
 * The main input is a {@link NextPaymentViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentViQueryService extends QueryService<NextPaymentVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViQueryService.class);

    private final NextPaymentViRepository nextPaymentViRepository;

    public NextPaymentViQueryService(NextPaymentViRepository nextPaymentViRepository) {
        this.nextPaymentViRepository = nextPaymentViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentVi> findByCriteria(NextPaymentViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentVi> specification = createSpecification(criteria);
        return nextPaymentViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentVi> specification = createSpecification(criteria);
        return nextPaymentViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentVi> createSpecification(NextPaymentViCriteria criteria) {
        Specification<NextPaymentVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentVi_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentVi_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentVi_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentVi_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextPaymentVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
