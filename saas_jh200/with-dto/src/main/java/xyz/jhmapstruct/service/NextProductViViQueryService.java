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
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.repository.NextProductViViRepository;
import xyz.jhmapstruct.service.criteria.NextProductViViCriteria;
import xyz.jhmapstruct.service.dto.NextProductViViDTO;
import xyz.jhmapstruct.service.mapper.NextProductViViMapper;

/**
 * Service for executing complex queries for {@link NextProductViVi} entities in the database.
 * The main input is a {@link NextProductViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductViViQueryService extends QueryService<NextProductViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViViQueryService.class);

    private final NextProductViViRepository nextProductViViRepository;

    private final NextProductViViMapper nextProductViViMapper;

    public NextProductViViQueryService(NextProductViViRepository nextProductViViRepository, NextProductViViMapper nextProductViViMapper) {
        this.nextProductViViRepository = nextProductViViRepository;
        this.nextProductViViMapper = nextProductViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductViViDTO> findByCriteria(NextProductViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductViVi> specification = createSpecification(criteria);
        return nextProductViViRepository.findAll(specification, page).map(nextProductViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductViVi> specification = createSpecification(criteria);
        return nextProductViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductViVi> createSpecification(NextProductViViCriteria criteria) {
        Specification<NextProductViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductViVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductViVi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductViVi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductViVi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductViVi_.category, JoinType.LEFT).get(NextCategoryViVi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductViVi_.order, JoinType.LEFT).get(NextOrderViVi_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductViVi_.suppliers, JoinType.LEFT).get(NextSupplierViVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
