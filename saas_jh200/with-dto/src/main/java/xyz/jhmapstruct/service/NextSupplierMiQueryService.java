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
import xyz.jhmapstruct.domain.NextSupplierMi;
import xyz.jhmapstruct.repository.NextSupplierMiRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierMiCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierMiDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierMiMapper;

/**
 * Service for executing complex queries for {@link NextSupplierMi} entities in the database.
 * The main input is a {@link NextSupplierMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierMiQueryService extends QueryService<NextSupplierMi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiQueryService.class);

    private final NextSupplierMiRepository nextSupplierMiRepository;

    private final NextSupplierMiMapper nextSupplierMiMapper;

    public NextSupplierMiQueryService(NextSupplierMiRepository nextSupplierMiRepository, NextSupplierMiMapper nextSupplierMiMapper) {
        this.nextSupplierMiRepository = nextSupplierMiRepository;
        this.nextSupplierMiMapper = nextSupplierMiMapper;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierMiDTO> findByCriteria(NextSupplierMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierMi> specification = createSpecification(criteria);
        return nextSupplierMiRepository
            .fetchBagRelationships(nextSupplierMiRepository.findAll(specification, page))
            .map(nextSupplierMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierMi> specification = createSpecification(criteria);
        return nextSupplierMiRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierMi> createSpecification(NextSupplierMiCriteria criteria) {
        Specification<NextSupplierMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierMi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierMi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierMi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierMi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierMi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierMi_.products, JoinType.LEFT).get(ProductMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
