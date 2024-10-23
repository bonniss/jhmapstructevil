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
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.repository.NextSupplierRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMapper;

/**
 * Service for executing complex queries for {@link NextSupplier} entities in the database.
 * The main input is a {@link NextSupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierQueryService extends QueryService<NextSupplier> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierQueryService.class);

    private final NextSupplierRepository nextSupplierRepository;

    private final NextSupplierMapper nextSupplierMapper;

    public NextSupplierQueryService(NextSupplierRepository nextSupplierRepository, NextSupplierMapper nextSupplierMapper) {
        this.nextSupplierRepository = nextSupplierRepository;
        this.nextSupplierMapper = nextSupplierMapper;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierDTO> findByCriteria(NextSupplierCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplier> specification = createSpecification(criteria);
        return nextSupplierRepository
            .fetchBagRelationships(nextSupplierRepository.findAll(specification, page))
            .map(nextSupplierMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplier> specification = createSpecification(criteria);
        return nextSupplierRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplier> createSpecification(NextSupplierCriteria criteria) {
        Specification<NextSupplier> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplier_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplier_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplier_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplier_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplier_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(NextSupplier_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplier_.products, JoinType.LEFT).get(NextProduct_.id)
                    )
                );
            }
        }
        return specification;
    }
}