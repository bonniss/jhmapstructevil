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
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;
import xyz.jhmapstruct.service.criteria.InvoiceMiCriteria;
import xyz.jhmapstruct.service.dto.InvoiceMiDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMiMapper;

/**
 * Service for executing complex queries for {@link InvoiceMi} entities in the database.
 * The main input is a {@link InvoiceMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceMiQueryService extends QueryService<InvoiceMi> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiQueryService.class);

    private final InvoiceMiRepository invoiceMiRepository;

    private final InvoiceMiMapper invoiceMiMapper;

    public InvoiceMiQueryService(InvoiceMiRepository invoiceMiRepository, InvoiceMiMapper invoiceMiMapper) {
        this.invoiceMiRepository = invoiceMiRepository;
        this.invoiceMiMapper = invoiceMiMapper;
    }

    /**
     * Return a {@link Page} of {@link InvoiceMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceMiDTO> findByCriteria(InvoiceMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceMi> specification = createSpecification(criteria);
        return invoiceMiRepository.findAll(specification, page).map(invoiceMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceMi> specification = createSpecification(criteria);
        return invoiceMiRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceMi> createSpecification(InvoiceMiCriteria criteria) {
        Specification<InvoiceMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceMi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceMi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceMi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceMi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceMi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
