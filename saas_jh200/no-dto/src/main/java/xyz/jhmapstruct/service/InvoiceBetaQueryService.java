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
import xyz.jhmapstruct.domain.InvoiceBeta;
import xyz.jhmapstruct.repository.InvoiceBetaRepository;
import xyz.jhmapstruct.service.criteria.InvoiceBetaCriteria;

/**
 * Service for executing complex queries for {@link InvoiceBeta} entities in the database.
 * The main input is a {@link InvoiceBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceBetaQueryService extends QueryService<InvoiceBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceBetaQueryService.class);

    private final InvoiceBetaRepository invoiceBetaRepository;

    public InvoiceBetaQueryService(InvoiceBetaRepository invoiceBetaRepository) {
        this.invoiceBetaRepository = invoiceBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link InvoiceBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceBeta> findByCriteria(InvoiceBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceBeta> specification = createSpecification(criteria);
        return invoiceBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceBeta> specification = createSpecification(criteria);
        return invoiceBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceBeta> createSpecification(InvoiceBetaCriteria criteria) {
        Specification<InvoiceBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceBeta_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceBeta_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceBeta_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceBeta_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceBeta_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
