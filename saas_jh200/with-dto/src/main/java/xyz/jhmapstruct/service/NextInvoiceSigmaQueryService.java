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
import xyz.jhmapstruct.domain.NextInvoiceSigma;
import xyz.jhmapstruct.repository.NextInvoiceSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceSigmaMapper;

/**
 * Service for executing complex queries for {@link NextInvoiceSigma} entities in the database.
 * The main input is a {@link NextInvoiceSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceSigmaQueryService extends QueryService<NextInvoiceSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceSigmaQueryService.class);

    private final NextInvoiceSigmaRepository nextInvoiceSigmaRepository;

    private final NextInvoiceSigmaMapper nextInvoiceSigmaMapper;

    public NextInvoiceSigmaQueryService(
        NextInvoiceSigmaRepository nextInvoiceSigmaRepository,
        NextInvoiceSigmaMapper nextInvoiceSigmaMapper
    ) {
        this.nextInvoiceSigmaRepository = nextInvoiceSigmaRepository;
        this.nextInvoiceSigmaMapper = nextInvoiceSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceSigmaDTO> findByCriteria(NextInvoiceSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceSigma> specification = createSpecification(criteria);
        return nextInvoiceSigmaRepository.findAll(specification, page).map(nextInvoiceSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceSigma> specification = createSpecification(criteria);
        return nextInvoiceSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceSigma> createSpecification(NextInvoiceSigmaCriteria criteria) {
        Specification<NextInvoiceSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceSigma_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceSigma_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceSigma_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceSigma_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceSigma_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
