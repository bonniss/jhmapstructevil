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
import xyz.jhmapstruct.domain.NextInvoiceMi;
import xyz.jhmapstruct.repository.NextInvoiceMiRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceMiCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceMiDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMiMapper;

/**
 * Service for executing complex queries for {@link NextInvoiceMi} entities in the database.
 * The main input is a {@link NextInvoiceMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceMiQueryService extends QueryService<NextInvoiceMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiQueryService.class);

    private final NextInvoiceMiRepository nextInvoiceMiRepository;

    private final NextInvoiceMiMapper nextInvoiceMiMapper;

    public NextInvoiceMiQueryService(NextInvoiceMiRepository nextInvoiceMiRepository, NextInvoiceMiMapper nextInvoiceMiMapper) {
        this.nextInvoiceMiRepository = nextInvoiceMiRepository;
        this.nextInvoiceMiMapper = nextInvoiceMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceMiDTO> findByCriteria(NextInvoiceMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceMi> specification = createSpecification(criteria);
        return nextInvoiceMiRepository.findAll(specification, page).map(nextInvoiceMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceMi> specification = createSpecification(criteria);
        return nextInvoiceMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceMi> createSpecification(NextInvoiceMiCriteria criteria) {
        Specification<NextInvoiceMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceMi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceMi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceMi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceMi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceMi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextInvoiceMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
