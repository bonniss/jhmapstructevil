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
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.criteria.ProductViCriteria;
import xyz.jhmapstruct.service.dto.ProductViDTO;
import xyz.jhmapstruct.service.mapper.ProductViMapper;

/**
 * Service for executing complex queries for {@link ProductVi} entities in the database.
 * The main input is a {@link ProductViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductViQueryService extends QueryService<ProductVi> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViQueryService.class);

    private final ProductViRepository productViRepository;

    private final ProductViMapper productViMapper;

    public ProductViQueryService(ProductViRepository productViRepository, ProductViMapper productViMapper) {
        this.productViRepository = productViRepository;
        this.productViMapper = productViMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductViDTO> findByCriteria(ProductViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductVi> specification = createSpecification(criteria);
        return productViRepository.findAll(specification, page).map(productViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductVi> specification = createSpecification(criteria);
        return productViRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductVi> createSpecification(ProductViCriteria criteria) {
        Specification<ProductVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductVi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductVi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductVi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(ProductVi_.category, JoinType.LEFT).get(CategoryVi_.id))
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductVi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductVi_.order, JoinType.LEFT).get(OrderVi_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root -> root.join(ProductVi_.suppliers, JoinType.LEFT).get(SupplierVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
