package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlVueVueVi;
import ai.realworld.repository.AlVueVueViRepository;
import ai.realworld.service.criteria.AlVueVueViCriteria;
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
 * Service for executing complex queries for {@link AlVueVueVi} entities in the database.
 * The main input is a {@link AlVueVueViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlVueVueVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlVueVueViQueryService extends QueryService<AlVueVueVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViQueryService.class);

    private final AlVueVueViRepository alVueVueViRepository;

    public AlVueVueViQueryService(AlVueVueViRepository alVueVueViRepository) {
        this.alVueVueViRepository = alVueVueViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlVueVueVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlVueVueVi> findByCriteria(AlVueVueViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlVueVueVi> specification = createSpecification(criteria);
        return alVueVueViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlVueVueViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlVueVueVi> specification = createSpecification(criteria);
        return alVueVueViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlVueVueViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlVueVueVi> createSpecification(AlVueVueViCriteria criteria) {
        Specification<AlVueVueVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlVueVueVi_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AlVueVueVi_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlVueVueVi_.name));
            }
            if (criteria.getContentHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentHeitiga(), AlVueVueVi_.contentHeitiga));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), AlVueVueVi_.discountType));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), AlVueVueVi_.discountRate));
            }
            if (criteria.getScope() != null) {
                specification = specification.and(buildSpecification(criteria.getScope(), AlVueVueVi_.scope));
            }
            if (criteria.getIsIndividuallyUsedOnly() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getIsIndividuallyUsedOnly(), AlVueVueVi_.isIndividuallyUsedOnly)
                );
            }
            if (criteria.getUsageLifeTimeLimit() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getUsageLifeTimeLimit(), AlVueVueVi_.usageLifeTimeLimit)
                );
            }
            if (criteria.getUsageLimitPerUser() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageLimitPerUser(), AlVueVueVi_.usageLimitPerUser));
            }
            if (criteria.getUsageQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageQuantity(), AlVueVueVi_.usageQuantity));
            }
            if (criteria.getMinimumSpend() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumSpend(), AlVueVueVi_.minimumSpend));
            }
            if (criteria.getMaximumSpend() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximumSpend(), AlVueVueVi_.maximumSpend));
            }
            if (criteria.getCanBeCollectedByUser() != null) {
                specification = specification.and(buildSpecification(criteria.getCanBeCollectedByUser(), AlVueVueVi_.canBeCollectedByUser));
            }
            if (criteria.getSalePriceFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePriceFromDate(), AlVueVueVi_.salePriceFromDate));
            }
            if (criteria.getSalePriceToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePriceToDate(), AlVueVueVi_.salePriceToDate));
            }
            if (criteria.getPublicationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPublicationStatus(), AlVueVueVi_.publicationStatus));
            }
            if (criteria.getPublishedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishedDate(), AlVueVueVi_.publishedDate));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlVueVueVi_.image, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getAlVueVueViUsageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAlVueVueViUsageId(), root ->
                        root.join(AlVueVueVi_.alVueVueViUsage, JoinType.LEFT).get(AlVueVueViUsage_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlVueVueVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getConditionsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getConditionsId(), root ->
                        root.join(AlVueVueVi_.conditions, JoinType.LEFT).get(AlVueVueViCondition_.id)
                    )
                );
            }
        }
        return specification;
    }
}
