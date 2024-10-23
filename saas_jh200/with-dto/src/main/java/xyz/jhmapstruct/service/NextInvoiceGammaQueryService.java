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
import xyz.jhmapstruct.domain.NextInvoiceGamma;
import xyz.jhmapstruct.repository.NextInvoiceGammaRepository;
import xyz.jhmapstruct.service.criteria.NextInvoiceGammaCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceGammaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceGammaMapper;

/**
 * Service for executing complex queries for {@link NextInvoiceGamma} entities in the database.
 * The main input is a {@link NextInvoiceGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextInvoiceGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextInvoiceGammaQueryService extends QueryService<NextInvoiceGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceGammaQueryService.class);

    private final NextInvoiceGammaRepository nextInvoiceGammaRepository;

    private final NextInvoiceGammaMapper nextInvoiceGammaMapper;

    public NextInvoiceGammaQueryService(
        NextInvoiceGammaRepository nextInvoiceGammaRepository,
        NextInvoiceGammaMapper nextInvoiceGammaMapper
    ) {
        this.nextInvoiceGammaRepository = nextInvoiceGammaRepository;
        this.nextInvoiceGammaMapper = nextInvoiceGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextInvoiceGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextInvoiceGammaDTO> findByCriteria(NextInvoiceGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextInvoiceGamma> specification = createSpecification(criteria);
        return nextInvoiceGammaRepository.findAll(specification, page).map(nextInvoiceGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextInvoiceGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextInvoiceGamma> specification = createSpecification(criteria);
        return nextInvoiceGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextInvoiceGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextInvoiceGamma> createSpecification(NextInvoiceGammaCriteria criteria) {
        Specification<NextInvoiceGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextInvoiceGamma_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), NextInvoiceGamma_.invoiceNumber));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), NextInvoiceGamma_.issueDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), NextInvoiceGamma_.dueDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextInvoiceGamma_.amount));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextInvoiceGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
