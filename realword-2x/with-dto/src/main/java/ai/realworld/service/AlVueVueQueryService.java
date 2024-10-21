package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlVueVue;
import ai.realworld.repository.AlVueVueRepository;
import ai.realworld.service.criteria.AlVueVueCriteria;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.mapper.AlVueVueMapper;
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
 * Service for executing complex queries for {@link AlVueVue} entities in the database.
 * The main input is a {@link AlVueVueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlVueVueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlVueVueQueryService extends QueryService<AlVueVue> {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueQueryService.class);

    private final AlVueVueRepository alVueVueRepository;

    private final AlVueVueMapper alVueVueMapper;

    public AlVueVueQueryService(AlVueVueRepository alVueVueRepository, AlVueVueMapper alVueVueMapper) {
        this.alVueVueRepository = alVueVueRepository;
        this.alVueVueMapper = alVueVueMapper;
    }

    /**
     * Return a {@link Page} of {@link AlVueVueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlVueVueDTO> findByCriteria(AlVueVueCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlVueVue> specification = createSpecification(criteria);
        return alVueVueRepository.findAll(specification, page).map(alVueVueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlVueVueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlVueVue> specification = createSpecification(criteria);
        return alVueVueRepository.count(specification);
    }

    /**
     * Function to convert {@link AlVueVueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlVueVue> createSpecification(AlVueVueCriteria criteria) {
        Specification<AlVueVue> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlVueVue_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), AlVueVue_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlVueVue_.name));
            }
            if (criteria.getContentHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentHeitiga(), AlVueVue_.contentHeitiga));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), AlVueVue_.discountType));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), AlVueVue_.discountRate));
            }
            if (criteria.getScope() != null) {
                specification = specification.and(buildSpecification(criteria.getScope(), AlVueVue_.scope));
            }
            if (criteria.getIsIndividuallyUsedOnly() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getIsIndividuallyUsedOnly(), AlVueVue_.isIndividuallyUsedOnly)
                );
            }
            if (criteria.getUsageLifeTimeLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageLifeTimeLimit(), AlVueVue_.usageLifeTimeLimit));
            }
            if (criteria.getUsageLimitPerUser() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageLimitPerUser(), AlVueVue_.usageLimitPerUser));
            }
            if (criteria.getUsageQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageQuantity(), AlVueVue_.usageQuantity));
            }
            if (criteria.getMinimumSpend() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumSpend(), AlVueVue_.minimumSpend));
            }
            if (criteria.getMaximumSpend() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximumSpend(), AlVueVue_.maximumSpend));
            }
            if (criteria.getCanBeCollectedByUser() != null) {
                specification = specification.and(buildSpecification(criteria.getCanBeCollectedByUser(), AlVueVue_.canBeCollectedByUser));
            }
            if (criteria.getSalePriceFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePriceFromDate(), AlVueVue_.salePriceFromDate));
            }
            if (criteria.getSalePriceToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePriceToDate(), AlVueVue_.salePriceToDate));
            }
            if (criteria.getPublicationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPublicationStatus(), AlVueVue_.publicationStatus));
            }
            if (criteria.getPublishedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishedDate(), AlVueVue_.publishedDate));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlVueVue_.image, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getAlVueVueUsageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAlVueVueUsageId(), root ->
                        root.join(AlVueVue_.alVueVueUsage, JoinType.LEFT).get(AlVueVueUsage_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlVueVue_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getConditionsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getConditionsId(), root ->
                        root.join(AlVueVue_.conditions, JoinType.LEFT).get(AlVueVueCondition_.id)
                    )
                );
            }
        }
        return specification;
    }
}
