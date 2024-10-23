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
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.repository.SupplierAlphaRepository;
import xyz.jhmapstruct.service.criteria.SupplierAlphaCriteria;
import xyz.jhmapstruct.service.dto.SupplierAlphaDTO;
import xyz.jhmapstruct.service.mapper.SupplierAlphaMapper;

/**
 * Service for executing complex queries for {@link SupplierAlpha} entities in the database.
 * The main input is a {@link SupplierAlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierAlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierAlphaQueryService extends QueryService<SupplierAlpha> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierAlphaQueryService.class);

    private final SupplierAlphaRepository supplierAlphaRepository;

    private final SupplierAlphaMapper supplierAlphaMapper;

    public SupplierAlphaQueryService(SupplierAlphaRepository supplierAlphaRepository, SupplierAlphaMapper supplierAlphaMapper) {
        this.supplierAlphaRepository = supplierAlphaRepository;
        this.supplierAlphaMapper = supplierAlphaMapper;
    }

    /**
     * Return a {@link Page} of {@link SupplierAlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierAlphaDTO> findByCriteria(SupplierAlphaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierAlpha> specification = createSpecification(criteria);
        return supplierAlphaRepository
            .fetchBagRelationships(supplierAlphaRepository.findAll(specification, page))
            .map(supplierAlphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierAlphaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierAlpha> specification = createSpecification(criteria);
        return supplierAlphaRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierAlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierAlpha> createSpecification(SupplierAlphaCriteria criteria) {
        Specification<SupplierAlpha> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierAlpha_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierAlpha_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierAlpha_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierAlpha_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierAlpha_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierAlpha_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(SupplierAlpha_.products, JoinType.LEFT).get(ProductAlpha_.id)
                    )
                );
            }
        }
        return specification;
    }
}
