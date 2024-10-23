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
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;
import xyz.jhmapstruct.service.criteria.InvoiceMiMiCriteria;

/**
 * Service for executing complex queries for {@link InvoiceMiMi} entities in the database.
 * The main input is a {@link InvoiceMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceMiMiQueryService extends QueryService<InvoiceMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiMiQueryService.class);

    private final InvoiceMiMiRepository invoiceMiMiRepository;

    public InvoiceMiMiQueryService(InvoiceMiMiRepository invoiceMiMiRepository) {
        this.invoiceMiMiRepository = invoiceMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link InvoiceMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceMiMi> findByCriteria(InvoiceMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceMiMi> specification = createSpecification(criteria);
        return invoiceMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceMiMi> specification = createSpecification(criteria);
        return invoiceMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceMiMi> createSpecification(InvoiceMiMiCriteria criteria) {
        Specification<InvoiceMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceMiMi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceMiMi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceMiMi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceMiMi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceMiMi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
