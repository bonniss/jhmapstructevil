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
import xyz.jhmapstruct.domain.InvoiceTheta;
import xyz.jhmapstruct.repository.InvoiceThetaRepository;
import xyz.jhmapstruct.service.criteria.InvoiceThetaCriteria;

/**
 * Service for executing complex queries for {@link InvoiceTheta} entities in the database.
 * The main input is a {@link InvoiceThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceThetaQueryService extends QueryService<InvoiceTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceThetaQueryService.class);

    private final InvoiceThetaRepository invoiceThetaRepository;

    public InvoiceThetaQueryService(InvoiceThetaRepository invoiceThetaRepository) {
        this.invoiceThetaRepository = invoiceThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link InvoiceTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceTheta> findByCriteria(InvoiceThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceTheta> specification = createSpecification(criteria);
        return invoiceThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceTheta> specification = createSpecification(criteria);
        return invoiceThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceTheta> createSpecification(InvoiceThetaCriteria criteria) {
        Specification<InvoiceTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceTheta_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceTheta_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceTheta_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceTheta_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceTheta_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
