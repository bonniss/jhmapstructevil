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
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.criteria.ProductMiCriteria;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.service.mapper.ProductMiMapper;

/**
 * Service for executing complex queries for {@link ProductMi} entities in the database.
 * The main input is a {@link ProductMiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductMiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductMiQueryService extends QueryService<ProductMi> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiQueryService.class);

    private final ProductMiRepository productMiRepository;

    private final ProductMiMapper productMiMapper;

    public ProductMiQueryService(ProductMiRepository productMiRepository, ProductMiMapper productMiMapper) {
        this.productMiRepository = productMiRepository;
        this.productMiMapper = productMiMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductMiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductMiDTO> findByCriteria(ProductMiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductMi> specification = createSpecification(criteria);
        return productMiRepository.findAll(specification, page).map(productMiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductMiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductMi> specification = createSpecification(criteria);
        return productMiRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductMiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductMi> createSpecification(ProductMiCriteria criteria) {
        Specification<ProductMi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductMi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductMi_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductMi_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductMi_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductMi_.category, JoinType.LEFT).get(NextCategoryMi_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductMi_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductMi_.order, JoinType.LEFT).get(OrderMi_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductMi_.suppliers, JoinType.LEFT).get(NextSupplierMi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
