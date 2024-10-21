package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AllMassageThaiVi;
import ai.realworld.repository.AllMassageThaiViRepository;
import ai.realworld.service.criteria.AllMassageThaiViCriteria;
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
 * Service for executing complex queries for {@link AllMassageThaiVi} entities in the database.
 * The main input is a {@link AllMassageThaiViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AllMassageThaiVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AllMassageThaiViQueryService extends QueryService<AllMassageThaiVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiViQueryService.class);

    private final AllMassageThaiViRepository allMassageThaiViRepository;

    public AllMassageThaiViQueryService(AllMassageThaiViRepository allMassageThaiViRepository) {
        this.allMassageThaiViRepository = allMassageThaiViRepository;
    }

    /**
     * Return a {@link Page} of {@link AllMassageThaiVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AllMassageThaiVi> findByCriteria(AllMassageThaiViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AllMassageThaiVi> specification = createSpecification(criteria);
        return allMassageThaiViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AllMassageThaiViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AllMassageThaiVi> specification = createSpecification(criteria);
        return allMassageThaiViRepository.count(specification);
    }

    /**
     * Function to convert {@link AllMassageThaiViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AllMassageThaiVi> createSpecification(AllMassageThaiViCriteria criteria) {
        Specification<AllMassageThaiVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AllMassageThaiVi_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AllMassageThaiVi_.title));
            }
            if (criteria.getTopContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTopContent(), AllMassageThaiVi_.topContent));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), AllMassageThaiVi_.content));
            }
            if (criteria.getBottomContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBottomContent(), AllMassageThaiVi_.bottomContent));
            }
            if (criteria.getPropTitleMappingJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPropTitleMappingJason(), AllMassageThaiVi_.propTitleMappingJason)
                );
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AllMassageThaiVi_.dataSourceMappingType)
                );
            }
            if (criteria.getTargetUrls() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTargetUrls(), AllMassageThaiVi_.targetUrls));
            }
            if (criteria.getThumbnailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThumbnailId(), root ->
                        root.join(AllMassageThaiVi_.thumbnail, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AllMassageThaiVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
