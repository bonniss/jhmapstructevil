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
import xyz.jhmapstruct.domain.NextInvoiceMiMi;
import xyz.jhmapstruct.repository.NextInvoiceMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMiMiMapper;

/**
 * Service for executing complex queries for {@link NextInvoiceMiMi} entities in the database.
 * The main input is a {@link NextInvoiceMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceMiMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceMiMiQueryService extends QueryService<NextInvoiceMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiMiQueryService.class);

    private final NextInvoiceMiMiRepository nextInvoiceMiMiRepository;

    private final NextInvoiceMiMiMapper nextInvoiceMiMiMapper;

    public NextInvoiceMiMiQueryService(NextInvoiceMiMiRepository nextInvoiceMiMiRepository, NextInvoiceMiMiMapper nextInvoiceMiMiMapper) {
        this.nextInvoiceMiMiRepository = nextInvoiceMiMiRepository;
        this.nextInvoiceMiMiMapper = nextInvoiceMiMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceMiMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceMiMiDTO> findByCriteria(NextInvoiceMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceMiMi> specification = createSpecification(criteria);
        return nextInvoiceMiMiRepository.findAll(specification, page).map(nextInvoiceMiMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceMiMi> specification = createSpecification(criteria);
        return nextInvoiceMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceMiMi> createSpecification(NextInvoiceMiMiCriteria criteria) {
        Specification<NextInvoiceMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceMiMi_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceMiMi_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceMiMi_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceMiMi_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceMiMi_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
