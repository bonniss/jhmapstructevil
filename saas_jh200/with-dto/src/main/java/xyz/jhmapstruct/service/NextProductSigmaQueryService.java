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
import xyz.jhmapstruct.domain.NextProductSigma;
import xyz.jhmapstruct.repository.NextProductSigmaRepository;
import xyz.jhmapstruct.service.criteria.NextProductSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextProductSigmaMapper;

/**
 * Service for executing complex queries for {@link NextProductSigma} entities in the database.
 * The main input is a {@link NextProductSigmaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductSigmaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductSigmaQueryService extends QueryService<NextProductSigma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductSigmaQueryService.class);

    private final NextProductSigmaRepository nextProductSigmaRepository;

    private final NextProductSigmaMapper nextProductSigmaMapper;

    public NextProductSigmaQueryService(
        NextProductSigmaRepository nextProductSigmaRepository,
        NextProductSigmaMapper nextProductSigmaMapper
    ) {
        this.nextProductSigmaRepository = nextProductSigmaRepository;
        this.nextProductSigmaMapper = nextProductSigmaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductSigmaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductSigmaDTO> findByCriteria(NextProductSigmaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductSigma> specification = createSpecification(criteria);
        return nextProductSigmaRepository.findAll(specification, page).map(nextProductSigmaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductSigmaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductSigma> specification = createSpecification(criteria);
        return nextProductSigmaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductSigmaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductSigma> createSpecification(NextProductSigmaCriteria criteria) {
        Specification<NextProductSigma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductSigma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductSigma_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductSigma_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductSigma_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductSigma_.category, JoinType.LEFT).get(NextCategorySigma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductSigma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductSigma_.order, JoinType.LEFT).get(NextOrderSigma_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductSigma_.suppliers, JoinType.LEFT).get(NextSupplierSigma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
