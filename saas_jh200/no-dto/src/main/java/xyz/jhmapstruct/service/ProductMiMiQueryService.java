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
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.criteria.ProductMiMiCriteria;

/**
 * Service for executing complex queries for {@link ProductMiMi} entities in the database.
 * The main input is a {@link ProductMiMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductMiMi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductMiMiQueryService extends QueryService<ProductMiMi> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiQueryService.class);

    private final ProductMiMiRepository productMiMiRepository;

    public ProductMiMiQueryService(ProductMiMiRepository productMiMiRepository) {
        this.productMiMiRepository = productMiMiRepository;
    }

    /**
     * Return a {@link Page} of {@link ProductMiMi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductMiMi> findByCriteria(ProductMiMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductMiMi> specification = createSpecification(criteria);
        return productMiMiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductMiMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductMiMi> specification = createSpecification(criteria);
        return productMiMiRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductMiMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductMiMi> createSpecification(ProductMiMiCriteria criteria) {
        Specification<ProductMiMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductMiMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductMiMi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductMiMi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductMiMi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductMiMi_.category, JoinType.LEFT).get(CategoryMiMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductMiMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductMiMi_.order, JoinType.LEFT).get(OrderMiMi_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductMiMi_.suppliers, JoinType.LEFT).get(SupplierMiMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
