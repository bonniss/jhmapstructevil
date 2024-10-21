package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AppMessageTemplate;
import ai.realworld.repository.AppMessageTemplateRepository;
import ai.realworld.service.criteria.AppMessageTemplateCriteria;
import ai.realworld.service.dto.AppMessageTemplateDTO;
import ai.realworld.service.mapper.AppMessageTemplateMapper;
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
 * Service for executing complex queries for {@link AppMessageTemplate} entities in the database.
 * The main input is a {@link AppMessageTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AppMessageTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppMessageTemplateQueryService extends QueryService<AppMessageTemplate> {

    private static final Logger LOG = LoggerFactory.getLogger(AppMessageTemplateQueryService.class);

    private final AppMessageTemplateRepository appMessageTemplateRepository;

    private final AppMessageTemplateMapper appMessageTemplateMapper;

    public AppMessageTemplateQueryService(
        AppMessageTemplateRepository appMessageTemplateRepository,
        AppMessageTemplateMapper appMessageTemplateMapper
    ) {
        this.appMessageTemplateRepository = appMessageTemplateRepository;
        this.appMessageTemplateMapper = appMessageTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link AppMessageTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppMessageTemplateDTO> findByCriteria(AppMessageTemplateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppMessageTemplate> specification = createSpecification(criteria);
        return appMessageTemplateRepository.findAll(specification, page).map(appMessageTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppMessageTemplateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AppMessageTemplate> specification = createSpecification(criteria);
        return appMessageTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link AppMessageTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppMessageTemplate> createSpecification(AppMessageTemplateCriteria criteria) {
        Specification<AppMessageTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppMessageTemplate_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AppMessageTemplate_.title));
            }
            if (criteria.getTopContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTopContent(), AppMessageTemplate_.topContent));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), AppMessageTemplate_.content));
            }
            if (criteria.getBottomContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBottomContent(), AppMessageTemplate_.bottomContent));
            }
            if (criteria.getPropTitleMappingJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPropTitleMappingJason(), AppMessageTemplate_.propTitleMappingJason)
                );
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AppMessageTemplate_.dataSourceMappingType)
                );
            }
            if (criteria.getTargetUrls() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTargetUrls(), AppMessageTemplate_.targetUrls));
            }
            if (criteria.getThumbnailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThumbnailId(), root ->
                        root.join(AppMessageTemplate_.thumbnail, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AppMessageTemplate_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}