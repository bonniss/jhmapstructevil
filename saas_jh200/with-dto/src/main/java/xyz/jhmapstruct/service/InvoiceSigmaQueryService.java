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
import xyz.jhmapstruct.domain.InvoiceSigma;
import xyz.jhmapstruct.repository.InvoiceSigmaRepository;
import xyz.jhmapstruct.service.criteria.InvoiceSigmaCriteria;
import xyz.jhmapstruct.service.dto.InvoiceSigmaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceSigmaMapper;

/**
 * Service for executing complex queries for {@link InvoiceSigma} entities in the database.
 * The main input is a {@link InvoiceSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceSigmaQueryService extends QueryService<InvoiceSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSigmaQueryService.class);

    private final InvoiceSigmaRepository invoiceSigmaRepository;

    private final InvoiceSigmaMapper invoiceSigmaMapper;

    public InvoiceSigmaQueryService(InvoiceSigmaRepository invoiceSigmaRepository, InvoiceSigmaMapper invoiceSigmaMapper) {
        this.invoiceSigmaRepository = invoiceSigmaRepository;
        this.invoiceSigmaMapper = invoiceSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link InvoiceSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceSigmaDTO> findByCriteria(InvoiceSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceSigma> specification = createSpecification(criteria);
        return invoiceSigmaRepository.findAll(specification, page).map(invoiceSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceSigma> specification = createSpecification(criteria);
        return invoiceSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceSigma> createSpecification(InvoiceSigmaCriteria criteria) {
        Specification<InvoiceSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceSigma_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceSigma_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceSigma_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceSigma_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceSigma_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
