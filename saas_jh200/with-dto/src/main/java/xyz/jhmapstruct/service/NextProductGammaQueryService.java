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
import xyz.jhmapstruct.domain.NextProductGamma;
import xyz.jhmapstruct.repository.NextProductGammaRepository;
import xyz.jhmapstruct.service.criteria.NextProductGammaCriteria;
import xyz.jhmapstruct.service.dto.NextProductGammaDTO;
import xyz.jhmapstruct.service.mapper.NextProductGammaMapper;

/**
 * Service for executing complex queries for {@link NextProductGamma} entities in the database.
 * The main input is a {@link NextProductGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductGammaQueryService extends QueryService<NextProductGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductGammaQueryService.class);

    private final NextProductGammaRepository nextProductGammaRepository;

    private final NextProductGammaMapper nextProductGammaMapper;

    public NextProductGammaQueryService(
        NextProductGammaRepository nextProductGammaRepository,
        NextProductGammaMapper nextProductGammaMapper
    ) {
        this.nextProductGammaRepository = nextProductGammaRepository;
        this.nextProductGammaMapper = nextProductGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductGammaDTO> findByCriteria(NextProductGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductGamma> specification = createSpecification(criteria);
        return nextProductGammaRepository.findAll(specification, page).map(nextProductGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductGamma> specification = createSpecification(criteria);
        return nextProductGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductGamma> createSpecification(NextProductGammaCriteria criteria) {
        Specification<NextProductGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductGamma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductGamma_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductGamma_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductGamma_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductGamma_.category, JoinType.LEFT).get(NextCategoryGamma_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductGamma_.order, JoinType.LEFT).get(NextOrderGamma_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductGamma_.suppliers, JoinType.LEFT).get(NextSupplierGamma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
