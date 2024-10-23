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
import xyz.jhmapstruct.domain.NextInvoice;
import xyz.jhmapstruct.repository.NextInvoiceRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceCriteria;

/**
 * Service for executing complex queries for {@link NextInvoice} entities in the database.
 * The main input is a {@link NextInvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceQueryService extends QueryService<NextInvoice> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceQueryService.class);

    private final NextInvoiceRepository nextInvoiceRepository;

    public NextInvoiceQueryService(NextInvoiceRepository nextInvoiceRepository) {
        this.nextInvoiceRepository = nextInvoiceRepository;
    }

    /**
     * Return a {@link Page} of {@link NextInvoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoice> findByCriteria(NextInvoiceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoice> specification = createSpecification(criteria);
        return nextInvoiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoice> specification = createSpecification(criteria);
        return nextInvoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoice> createSpecification(NextInvoiceCriteria criteria) {
        Specification<NextInvoice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoice_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoice_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoice_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoice_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoice_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextInvoice_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
