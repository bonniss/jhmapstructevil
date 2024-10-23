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
import xyz.jhmapstruct.domain.NextProductAlpha;
import xyz.jhmapstruct.repository.NextProductAlphaRepository;
import xyz.jhmapstruct.service.criteria.NextProductAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextProductAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextProductAlphaMapper;

/**
 * Service for executing complex queries for {@link NextProductAlpha} entities in the database.
 * The main input is a {@link NextProductAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductAlphaQueryService extends QueryService<NextProductAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductAlphaQueryService.class);

    private final NextProductAlphaRepository nextProductAlphaRepository;

    private final NextProductAlphaMapper nextProductAlphaMapper;

    public NextProductAlphaQueryService(
        NextProductAlphaRepository nextProductAlphaRepository,
        NextProductAlphaMapper nextProductAlphaMapper
    ) {
        this.nextProductAlphaRepository = nextProductAlphaRepository;
        this.nextProductAlphaMapper = nextProductAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductAlphaDTO> findByCriteria(NextProductAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductAlpha> specification = createSpecification(criteria);
        return nextProductAlphaRepository.findAll(specification, page).map(nextProductAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductAlpha> specification = createSpecification(criteria);
        return nextProductAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductAlpha> createSpecification(NextProductAlphaCriteria criteria) {
        Specification<NextProductAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductAlpha_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductAlpha_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductAlpha_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductAlpha_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductAlpha_.category, JoinType.LEFT).get(NextCategoryAlpha_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductAlpha_.order, JoinType.LEFT).get(NextOrderAlpha_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductAlpha_.suppliers, JoinType.LEFT).get(NextSupplierAlpha_.id)
                    )
                );
            }
        }
        return specification;
    }
}
