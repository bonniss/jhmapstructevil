package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AllMassageThai;
import ai.realworld.repository.AllMassageThaiRepository;
import ai.realworld.service.criteria.AllMassageThaiCriteria;
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
 * Service for executing complex queries for {@link AllMassageThai} entities in the database.
 * The main input is a {@link AllMassageThaiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AllMassageThai} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AllMassageThaiQueryService extends QueryService<AllMassageThai> {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiQueryService.class);

    private final AllMassageThaiRepository allMassageThaiRepository;

    public AllMassageThaiQueryService(AllMassageThaiRepository allMassageThaiRepository) {
        this.allMassageThaiRepository = allMassageThaiRepository;
    }

    /**
     * Return a {@link Page} of {@link AllMassageThai} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AllMassageThai> findByCriteria(AllMassageThaiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AllMassageThai> specification = createSpecification(criteria);
        return allMassageThaiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AllMassageThaiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AllMassageThai> specification = createSpecification(criteria);
        return allMassageThaiRepository.count(specification);
    }

    /**
     * Function to convert {@link AllMassageThaiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AllMassageThai> createSpecification(AllMassageThaiCriteria criteria) {
        Specification<AllMassageThai> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AllMassageThai_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AllMassageThai_.title));
            }
            if (criteria.getTopContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTopContent(), AllMassageThai_.topContent));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), AllMassageThai_.content));
            }
            if (criteria.getBottomContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBottomContent(), AllMassageThai_.bottomContent));
            }
            if (criteria.getPropTitleMappingJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPropTitleMappingJason(), AllMassageThai_.propTitleMappingJason)
                );
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AllMassageThai_.dataSourceMappingType)
                );
            }
            if (criteria.getTargetUrls() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTargetUrls(), AllMassageThai_.targetUrls));
            }
            if (criteria.getThumbnailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThumbnailId(), root ->
                        root.join(AllMassageThai_.thumbnail, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AllMassageThai_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
