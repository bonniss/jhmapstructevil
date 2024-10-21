package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPedroTax;
import ai.realworld.repository.AlPedroTaxRepository;
import ai.realworld.service.criteria.AlPedroTaxCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlPedroTax} entities in the database.
 * The main input is a {@link AlPedroTaxCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPedroTax} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPedroTaxQueryService extends QueryService<AlPedroTax> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxQueryService.class);

    private final AlPedroTaxRepository alPedroTaxRepository;

    public AlPedroTaxQueryService(AlPedroTaxRepository alPedroTaxRepository) {
        this.alPedroTaxRepository = alPedroTaxRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPedroTax} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPedroTax> findByCriteria(AlPedroTaxCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPedroTax> specification = createSpecification(criteria);
        return alPedroTaxRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPedroTaxCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPedroTax> specification = createSpecification(criteria);
        return alPedroTaxRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPedroTaxCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPedroTax> createSpecification(AlPedroTaxCriteria criteria) {
        Specification<AlPedroTax> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPedroTax_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlPedroTax_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlPedroTax_.description));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlPedroTax_.weight));
            }
            if (criteria.getPropertyType() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertyType(), AlPedroTax_.propertyType));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPedroTax_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAttributeTermsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeTermsId(), root ->
                        root.join(AlPedroTax_.attributeTerms, JoinType.LEFT).get(AlPounder_.id)
                    )
                );
            }
        }
        return specification;
    }
}
