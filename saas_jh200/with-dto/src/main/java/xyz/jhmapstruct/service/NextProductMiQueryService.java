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
import xyz.jhmapstruct.domain.NextProductMi;
import xyz.jhmapstruct.repository.NextProductMiRepository;
import xyz.jhmapstruct.service.criteria.NextProductMiCriteria;
import xyz.jhmapstruct.service.dto.NextProductMiDTO;
import xyz.jhmapstruct.service.mapper.NextProductMiMapper;

/**
 * Service for executing complex queries for {@link NextProductMi} entities in the database.
 * The main input is a {@link NextProductMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductMiQueryService extends QueryService<NextProductMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiQueryService.class);

    private final NextProductMiRepository nextProductMiRepository;

    private final NextProductMiMapper nextProductMiMapper;

    public NextProductMiQueryService(NextProductMiRepository nextProductMiRepository, NextProductMiMapper nextProductMiMapper) {
        this.nextProductMiRepository = nextProductMiRepository;
        this.nextProductMiMapper = nextProductMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductMiDTO> findByCriteria(NextProductMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductMi> specification = createSpecification(criteria);
        return nextProductMiRepository.findAll(specification, page).map(nextProductMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductMi> specification = createSpecification(criteria);
        return nextProductMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductMi> createSpecification(NextProductMiCriteria criteria) {
        Specification<NextProductMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductMi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductMi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductMi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductMi_.category, JoinType.LEFT).get(CategoryMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextProductMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(NextProductMi_.order, JoinType.LEFT).get(NextOrderMi_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductMi_.suppliers, JoinType.LEFT).get(SupplierMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
