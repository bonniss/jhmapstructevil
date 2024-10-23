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
import xyz.jhmapstruct.domain.InvoiceGamma;
import xyz.jhmapstruct.repository.InvoiceGammaRepository;
import xyz.jhmapstruct.service.criteria.InvoiceGammaCriteria;

/**
 * Service for executing complex queries for {@link InvoiceGamma} entities in the database.
 * The main input is a {@link InvoiceGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceGamma} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceGammaQueryService extends QueryService<InvoiceGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceGammaQueryService.class);

    private final InvoiceGammaRepository invoiceGammaRepository;

    public InvoiceGammaQueryService(InvoiceGammaRepository invoiceGammaRepository) {
        this.invoiceGammaRepository = invoiceGammaRepository;
    }

    /**
     * Return a {@link Page} of {@link InvoiceGamma} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceGamma> findByCriteria(InvoiceGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceGamma> specification = createSpecification(criteria);
        return invoiceGammaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceGamma> specification = createSpecification(criteria);
        return invoiceGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceGamma> createSpecification(InvoiceGammaCriteria criteria) {
        Specification<InvoiceGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceGamma_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceGamma_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceGamma_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceGamma_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceGamma_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
