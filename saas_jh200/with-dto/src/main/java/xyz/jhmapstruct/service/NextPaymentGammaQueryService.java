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
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.repository.NextPaymentGammaRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentGammaCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentGammaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentGammaMapper;

/**
 * Service for executing complex queries for {@link NextPaymentGamma} entities in the database.
 * The main input is a {@link NextPaymentGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentGammaQueryService extends QueryService<NextPaymentGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentGammaQueryService.class);

    private final NextPaymentGammaRepository nextPaymentGammaRepository;

    private final NextPaymentGammaMapper nextPaymentGammaMapper;

    public NextPaymentGammaQueryService(
        NextPaymentGammaRepository nextPaymentGammaRepository,
        NextPaymentGammaMapper nextPaymentGammaMapper
    ) {
        this.nextPaymentGammaRepository = nextPaymentGammaRepository;
        this.nextPaymentGammaMapper = nextPaymentGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentGammaDTO> findByCriteria(NextPaymentGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentGamma> specification = createSpecification(criteria);
        return nextPaymentGammaRepository.findAll(specification, page).map(nextPaymentGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentGamma> specification = createSpecification(criteria);
        return nextPaymentGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentGamma> createSpecification(NextPaymentGammaCriteria criteria) {
        Specification<NextPaymentGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentGamma_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentGamma_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentGamma_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentGamma_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
