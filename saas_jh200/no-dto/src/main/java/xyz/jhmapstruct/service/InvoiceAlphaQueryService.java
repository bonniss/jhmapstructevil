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
import xyz.jhmapstruct.domain.InvoiceAlpha;
import xyz.jhmapstruct.repository.InvoiceAlphaRepository;
import xyz.jhmapstruct.service.criteria.InvoiceAlphaCriteria;

/**
 * Service for executing complex queries for {@link InvoiceAlpha} entities in the database.
 * The main input is a {@link InvoiceAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceAlpha} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceAlphaQueryService extends QueryService<InvoiceAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceAlphaQueryService.class);

    private final InvoiceAlphaRepository invoiceAlphaRepository;

    public InvoiceAlphaQueryService(InvoiceAlphaRepository invoiceAlphaRepository) {
        this.invoiceAlphaRepository = invoiceAlphaRepository;
    }

    /**
     * Return a {@link Page} of {@link InvoiceAlpha} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceAlpha> findByCriteria(InvoiceAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceAlpha> specification = createSpecification(criteria);
        return invoiceAlphaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceAlpha> specification = createSpecification(criteria);
        return invoiceAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceAlpha> createSpecification(InvoiceAlphaCriteria criteria) {
        Specification<InvoiceAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceAlpha_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceAlpha_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceAlpha_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceAlpha_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceAlpha_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
