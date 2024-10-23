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
import xyz.jhmapstruct.domain.NextProductVi;
import xyz.jhmapstruct.repository.NextProductViRepository;
import xyz.jhmapstruct.service.criteria.NextProductViCriteria;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.service.mapper.NextProductViMapper;

/**
 * Service for executing complex queries for {@link NextProductVi} entities in the database.
 * The main input is a {@link NextProductViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductViQueryService extends QueryService<NextProductVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViQueryService.class);

    private final NextProductViRepository nextProductViRepository;

    private final NextProductViMapper nextProductViMapper;

    public NextProductViQueryService(NextProductViRepository nextProductViRepository, NextProductViMapper nextProductViMapper) {
        this.nextProductViRepository = nextProductViRepository;
        this.nextProductViMapper = nextProductViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductViDTO> findByCriteria(NextProductViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductVi> specification = createSpecification(criteria);
        return nextProductViRepository.findAll(specification, page).map(nextProductViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductVi> specification = createSpecification(criteria);
        return nextProductViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductVi> createSpecification(NextProductViCriteria criteria) {
        Specification<NextProductVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductVi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductVi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductVi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductVi_.category, JoinType.LEFT).get(NextCategoryVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextProductVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(NextProductVi_.order, JoinType.LEFT).get(NextOrderVi_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductVi_.suppliers, JoinType.LEFT).get(NextSupplierVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
