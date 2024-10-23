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
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.criteria.InvoiceViViCriteria;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViViMapper;

/**
 * Service for executing complex queries for {@link InvoiceViVi} entities in the database.
 * The main input is a {@link InvoiceViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InvoiceViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceViViQueryService extends QueryService<InvoiceViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViViQueryService.class);

    private final InvoiceViViRepository invoiceViViRepository;

    private final InvoiceViViMapper invoiceViViMapper;

    public InvoiceViViQueryService(InvoiceViViRepository invoiceViViRepository, InvoiceViViMapper invoiceViViMapper) {
        this.invoiceViViRepository = invoiceViViRepository;
        this.invoiceViViMapper = invoiceViViMapper;
    }

    /**
     * Return a {@link Page} of {@link InvoiceViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceViViDTO> findByCriteria(InvoiceViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceViVi> specification = createSpecification(criteria);
        return invoiceViViRepository.findAll(specification, page).map(invoiceViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InvoiceViVi> specification = createSpecification(criteria);
        return invoiceViViRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceViVi> createSpecification(InvoiceViViCriteria criteria) {
        Specification<InvoiceViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceViVi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), InvoiceViVi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), InvoiceViVi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), InvoiceViVi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvoiceViVi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(InvoiceViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
        }
        return specification;
    }
}
