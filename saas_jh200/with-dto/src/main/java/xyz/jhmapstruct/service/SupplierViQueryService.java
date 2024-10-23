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
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.criteria.SupplierViCriteria;
import xyz.jhmapstruct.service.dto.SupplierViDTO;
import xyz.jhmapstruct.service.mapper.SupplierViMapper;

/**
 * Service for executing complex queries for {@link SupplierVi} entities in the database.
 * The main input is a {@link SupplierViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SupplierViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierViQueryService extends QueryService<SupplierVi> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViQueryService.class);

    private final SupplierViRepository supplierViRepository;

    private final SupplierViMapper supplierViMapper;

    public SupplierViQueryService(SupplierViRepository supplierViRepository, SupplierViMapper supplierViMapper) {
        this.supplierViRepository = supplierViRepository;
        this.supplierViMapper = supplierViMapper;
    }

    /**
     * Return a {@link Page} of {@link SupplierViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SupplierViDTO> findByCriteria(SupplierViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SupplierVi> specification = createSpecification(criteria);
        return supplierViRepository.fetchBagRelationships(supplierViRepository.findAll(specification, page)).map(supplierViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SupplierVi> specification = createSpecification(criteria);
        return supplierViRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SupplierVi> createSpecification(SupplierViCriteria criteria) {
        Specification<SupplierVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SupplierVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SupplierVi_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), SupplierVi_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), SupplierVi_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), SupplierVi_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(SupplierVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root -> root.join(SupplierVi_.products, JoinType.LEFT).get(ProductVi_.id))
                );
            }
        }
        return specification;
    }
}
