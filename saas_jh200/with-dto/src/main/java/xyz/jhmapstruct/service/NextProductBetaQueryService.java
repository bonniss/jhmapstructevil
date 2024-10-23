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
import xyz.jhmapstruct.domain.NextProductBeta;
import xyz.jhmapstruct.repository.NextProductBetaRepository;
import xyz.jhmapstruct.service.criteria.NextProductBetaCriteria;
import xyz.jhmapstruct.service.dto.NextProductBetaDTO;
import xyz.jhmapstruct.service.mapper.NextProductBetaMapper;

/**
 * Service for executing complex queries for {@link NextProductBeta} entities in the database.
 * The main input is a {@link NextProductBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductBetaQueryService extends QueryService<NextProductBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductBetaQueryService.class);

    private final NextProductBetaRepository nextProductBetaRepository;

    private final NextProductBetaMapper nextProductBetaMapper;

    public NextProductBetaQueryService(NextProductBetaRepository nextProductBetaRepository, NextProductBetaMapper nextProductBetaMapper) {
        this.nextProductBetaRepository = nextProductBetaRepository;
        this.nextProductBetaMapper = nextProductBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductBetaDTO> findByCriteria(NextProductBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductBeta> specification = createSpecification(criteria);
        return nextProductBetaRepository.findAll(specification, page).map(nextProductBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductBeta> specification = createSpecification(criteria);
        return nextProductBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductBeta> createSpecification(NextProductBetaCriteria criteria) {
        Specification<NextProductBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductBeta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductBeta_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductBeta_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductBeta_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductBeta_.category, JoinType.LEFT).get(NextCategoryBeta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductBeta_.order, JoinType.LEFT).get(NextOrderBeta_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductBeta_.suppliers, JoinType.LEFT).get(NextSupplierBeta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
