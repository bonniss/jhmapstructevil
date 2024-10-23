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
import xyz.jhmapstruct.domain.NextInvoiceVi;
import xyz.jhmapstruct.repository.NextInvoiceViRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceViCriteria;

/**
 * Service for executing complex queries for {@link NextInvoiceVi} entities in the database.
 * The main input is a {@link NextInvoiceViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceViQueryService extends QueryService<NextInvoiceVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViQueryService.class);

    private final NextInvoiceViRepository nextInvoiceViRepository;

    public NextInvoiceViQueryService(NextInvoiceViRepository nextInvoiceViRepository) {
        this.nextInvoiceViRepository = nextInvoiceViRepository;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceVi> findByCriteria(NextInvoiceViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceVi> specification = createSpecification(criteria);
        return nextInvoiceViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceVi> specification = createSpecification(criteria);
        return nextInvoiceViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceVi> createSpecification(NextInvoiceViCriteria criteria) {
        Specification<NextInvoiceVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceVi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceVi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceVi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceVi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceVi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextInvoiceVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
