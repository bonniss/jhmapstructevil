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
import xyz.jhmapstruct.domain.NextInvoiceTheta;
import xyz.jhmapstruct.repository.NextInvoiceThetaRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceThetaCriteria;

/**
 * Service for executing complex queries for {@link NextInvoiceTheta} entities in the database.
 * The main input is a {@link NextInvoiceThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceTheta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceThetaQueryService extends QueryService<NextInvoiceTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceThetaQueryService.class);

    private final NextInvoiceThetaRepository nextInvoiceThetaRepository;

    public NextInvoiceThetaQueryService(NextInvoiceThetaRepository nextInvoiceThetaRepository) {
        this.nextInvoiceThetaRepository = nextInvoiceThetaRepository;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceTheta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceTheta> findByCriteria(NextInvoiceThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceTheta> specification = createSpecification(criteria);
        return nextInvoiceThetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceTheta> specification = createSpecification(criteria);
        return nextInvoiceThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceTheta> createSpecification(NextInvoiceThetaCriteria criteria) {
        Specification<NextInvoiceTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceTheta_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceTheta_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceTheta_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceTheta_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceTheta_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
