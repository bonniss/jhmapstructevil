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
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.repository.NextPaymentAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentAlphaMapper;

/**
 * Service for executing complex queries for {@link NextPaymentAlpha} entities in the database.
 * The main input is a {@link NextPaymentAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentAlphaQueryService extends QueryService<NextPaymentAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentAlphaQueryService.class);

    private final NextPaymentAlphaRepository nextPaymentAlphaRepository;

    private final NextPaymentAlphaMapper nextPaymentAlphaMapper;

    public NextPaymentAlphaQueryService(
        NextPaymentAlphaRepository nextPaymentAlphaRepository,
        NextPaymentAlphaMapper nextPaymentAlphaMapper
    ) {
        this.nextPaymentAlphaRepository = nextPaymentAlphaRepository;
        this.nextPaymentAlphaMapper = nextPaymentAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentAlphaDTO> findByCriteria(NextPaymentAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentAlpha> specification = createSpecification(criteria);
        return nextPaymentAlphaRepository.findAll(specification, page).map(nextPaymentAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentAlpha> specification = createSpecification(criteria);
        return nextPaymentAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentAlpha> createSpecification(NextPaymentAlphaCriteria criteria) {
        Specification<NextPaymentAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentAlpha_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentAlpha_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentAlpha_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentAlpha_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}