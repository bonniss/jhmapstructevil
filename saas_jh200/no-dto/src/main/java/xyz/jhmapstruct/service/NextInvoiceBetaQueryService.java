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
import xyz.jhmapstruct.domain.NextInvoiceBeta;
import xyz.jhmapstruct.repository.NextInvoiceBetaRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceBetaCriteria;

/**
 * Service for executing complex queries for {@link NextInvoiceBeta} entities in the database.
 * The main input is a {@link NextInvoiceBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceBeta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceBetaQueryService extends QueryService<NextInvoiceBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceBetaQueryService.class);

    private final NextInvoiceBetaRepository nextInvoiceBetaRepository;

    public NextInvoiceBetaQueryService(NextInvoiceBetaRepository nextInvoiceBetaRepository) {
        this.nextInvoiceBetaRepository = nextInvoiceBetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceBeta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceBeta> findByCriteria(NextInvoiceBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceBeta> specification = createSpecification(criteria);
        return nextInvoiceBetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceBeta> specification = createSpecification(criteria);
        return nextInvoiceBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceBeta> createSpecification(NextInvoiceBetaCriteria criteria) {
        Specification<NextInvoiceBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceBeta_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceBeta_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceBeta_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceBeta_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceBeta_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
