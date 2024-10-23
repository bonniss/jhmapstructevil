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
import xyz.jhmapstruct.domain.NextInvoiceAlpha;
import xyz.jhmapstruct.repository.NextInvoiceAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceAlphaMapper;

/**
 * Service for executing complex queries for {@link NextInvoiceAlpha} entities in the database.
 * The main input is a {@link NextInvoiceAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceAlphaQueryService extends QueryService<NextInvoiceAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceAlphaQueryService.class);

    private final NextInvoiceAlphaRepository nextInvoiceAlphaRepository;

    private final NextInvoiceAlphaMapper nextInvoiceAlphaMapper;

    public NextInvoiceAlphaQueryService(
        NextInvoiceAlphaRepository nextInvoiceAlphaRepository,
        NextInvoiceAlphaMapper nextInvoiceAlphaMapper
    ) {
        this.nextInvoiceAlphaRepository = nextInvoiceAlphaRepository;
        this.nextInvoiceAlphaMapper = nextInvoiceAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceAlphaDTO> findByCriteria(NextInvoiceAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceAlpha> specification = createSpecification(criteria);
        return nextInvoiceAlphaRepository.findAll(specification, page).map(nextInvoiceAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceAlpha> specification = createSpecification(criteria);
        return nextInvoiceAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceAlpha> createSpecification(NextInvoiceAlphaCriteria criteria) {
        Specification<NextInvoiceAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceAlpha_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceAlpha_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceAlpha_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceAlpha_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceAlpha_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
