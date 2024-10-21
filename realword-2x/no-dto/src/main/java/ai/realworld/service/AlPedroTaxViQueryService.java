package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.repository.AlPedroTaxViRepository;
import ai.realworld.service.criteria.AlPedroTaxViCriteria;
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
 * Service for executing complex queries for {@link AlPedroTaxVi} entities in the database.
 * The main input is a {@link AlPedroTaxViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPedroTaxVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPedroTaxViQueryService extends QueryService<AlPedroTaxVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxViQueryService.class);

    private final AlPedroTaxViRepository alPedroTaxViRepository;

    public AlPedroTaxViQueryService(AlPedroTaxViRepository alPedroTaxViRepository) {
        this.alPedroTaxViRepository = alPedroTaxViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPedroTaxVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPedroTaxVi> findByCriteria(AlPedroTaxViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPedroTaxVi> specification = createSpecification(criteria);
        return alPedroTaxViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPedroTaxViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPedroTaxVi> specification = createSpecification(criteria);
        return alPedroTaxViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPedroTaxViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPedroTaxVi> createSpecification(AlPedroTaxViCriteria criteria) {
        Specification<AlPedroTaxVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPedroTaxVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlPedroTaxVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlPedroTaxVi_.description));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlPedroTaxVi_.weight));
            }
            if (criteria.getPropertyType() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertyType(), AlPedroTaxVi_.propertyType));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPedroTaxVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getAttributeTermsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeTermsId(), root ->
                        root.join(AlPedroTaxVi_.attributeTerms, JoinType.LEFT).get(AlPounderVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
