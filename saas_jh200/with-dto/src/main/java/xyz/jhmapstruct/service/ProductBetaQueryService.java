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
import xyz.jhmapstruct.domain.ProductBeta;
import xyz.jhmapstruct.repository.ProductBetaRepository;
import xyz.jhmapstruct.service.criteria.ProductBetaCriteria;
import xyz.jhmapstruct.service.dto.ProductBetaDTO;
import xyz.jhmapstruct.service.mapper.ProductBetaMapper;

/**
 * Service for executing complex queries for {@link ProductBeta} entities in the database.
 * The main input is a {@link ProductBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductBetaQueryService extends QueryService<ProductBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductBetaQueryService.class);

    private final ProductBetaRepository productBetaRepository;

    private final ProductBetaMapper productBetaMapper;

    public ProductBetaQueryService(ProductBetaRepository productBetaRepository, ProductBetaMapper productBetaMapper) {
        this.productBetaRepository = productBetaRepository;
        this.productBetaMapper = productBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductBetaDTO> findByCriteria(ProductBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductBeta> specification = createSpecification(criteria);
        return productBetaRepository.findAll(specification, page).map(productBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ProductBeta> specification = createSpecification(criteria);
        return productBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductBeta> createSpecification(ProductBetaCriteria criteria) {
        Specification<ProductBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductBeta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductBeta_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ProductBeta_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), ProductBeta_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(ProductBeta_.category, JoinType.LEFT).get(CategoryBeta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root -> root.join(ProductBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id))
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(ProductBeta_.order, JoinType.LEFT).get(OrderBeta_.id))
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(ProductBeta_.suppliers, JoinType.LEFT).get(SupplierBeta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
