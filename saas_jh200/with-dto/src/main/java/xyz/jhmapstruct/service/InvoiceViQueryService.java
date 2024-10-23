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
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.criteria.InvoiceViCriteria;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViMapper;

/**
 * Service for executing complex queries for {@link InvoiceVi} entities in the database.
 * The main input is a {@link InvoiceViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceViQueryService extends QueryService<InvoiceVi> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViQueryService.class);

    private final InvoiceViRepository invoiceViRepository;

    private final InvoiceViMapper invoiceViMapper;

    public InvoiceViQueryService(InvoiceViRepository invoiceViRepository, InvoiceViMapper invoiceViMapper) {
        this.invoiceViRepository = invoiceViRepository;
        this.invoiceViMapper = invoiceViMapper;
    }

    /**
     * Return a {@link Page} of {@link InvoiceViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceViDTO> findByCriteria(InvoiceViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceVi> specification = createSpecification(criteria);
        return invoiceViRepository.findAll(specification, page).map(invoiceViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceVi> specification = createSpecification(criteria);
        return invoiceViRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceVi> createSpecification(InvoiceViCriteria criteria) {
        Specification<InvoiceVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceVi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceVi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceVi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceVi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceVi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
