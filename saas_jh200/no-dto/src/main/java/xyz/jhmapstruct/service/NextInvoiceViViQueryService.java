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
import xyz.jhmapstruct.domain.NextInvoiceViVi;
import xyz.jhmapstruct.repository.NextInvoiceViViRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceViViCriteria;

/**
 * Service for executing complex queries for {@link NextInvoiceViVi} entities in the database.
 * The main input is a {@link NextInvoiceViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceViVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceViViQueryService extends QueryService<NextInvoiceViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViViQueryService.class);

    private final NextInvoiceViViRepository nextInvoiceViViRepository;

    public NextInvoiceViViQueryService(NextInvoiceViViRepository nextInvoiceViViRepository) {
        this.nextInvoiceViViRepository = nextInvoiceViViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceViVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceViVi> findByCriteria(NextInvoiceViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceViVi> specification = createSpecification(criteria);
        return nextInvoiceViViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceViVi> specification = createSpecification(criteria);
        return nextInvoiceViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceViVi> createSpecification(NextInvoiceViViCriteria criteria) {
        Specification<NextInvoiceViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceViVi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceViVi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceViVi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceViVi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceViVi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
