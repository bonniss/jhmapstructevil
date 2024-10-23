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
import xyz.jhmapstruct.domain.NextProductMiMi;
import xyz.jhmapstruct.repository.NextProductMiMiRepository;
import xyz.jhmapstruct.service.criteria.NextProductMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextProductMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextProductMiMiMapper;

/**
 * Service for executing complex queries for {@link NextProductMiMi} entities in the database.
 * The main input is a {@link NextProductMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductMiMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductMiMiQueryService extends QueryService<NextProductMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiMiQueryService.class);

    private final NextProductMiMiRepository nextProductMiMiRepository;

    private final NextProductMiMiMapper nextProductMiMiMapper;

    public NextProductMiMiQueryService(NextProductMiMiRepository nextProductMiMiRepository, NextProductMiMiMapper nextProductMiMiMapper) {
        this.nextProductMiMiRepository = nextProductMiMiRepository;
        this.nextProductMiMiMapper = nextProductMiMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductMiMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductMiMiDTO> findByCriteria(NextProductMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductMiMi> specification = createSpecification(criteria);
        return nextProductMiMiRepository.findAll(specification, page).map(nextProductMiMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductMiMi> specification = createSpecification(criteria);
        return nextProductMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductMiMi> createSpecification(NextProductMiMiCriteria criteria) {
        Specification<NextProductMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductMiMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductMiMi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductMiMi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductMiMi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductMiMi_.category, JoinType.LEFT).get(NextCategoryMiMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductMiMi_.order, JoinType.LEFT).get(NextOrderMiMi_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductMiMi_.suppliers, JoinType.LEFT).get(NextSupplierMiMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
