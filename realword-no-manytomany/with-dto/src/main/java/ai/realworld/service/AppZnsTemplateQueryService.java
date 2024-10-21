package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AppZnsTemplate;
import ai.realworld.repository.AppZnsTemplateRepository;
import ai.realworld.service.criteria.AppZnsTemplateCriteria;
import ai.realworld.service.dto.AppZnsTemplateDTO;
import ai.realworld.service.mapper.AppZnsTemplateMapper;
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
 * Service for executing complex queries for {@link AppZnsTemplate} entities in the database.
 * The main input is a {@link AppZnsTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AppZnsTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppZnsTemplateQueryService extends QueryService<AppZnsTemplate> {

    private static final Logger LOG = LoggerFactory.getLogger(AppZnsTemplateQueryService.class);

    private final AppZnsTemplateRepository appZnsTemplateRepository;

    private final AppZnsTemplateMapper appZnsTemplateMapper;

    public AppZnsTemplateQueryService(AppZnsTemplateRepository appZnsTemplateRepository, AppZnsTemplateMapper appZnsTemplateMapper) {
        this.appZnsTemplateRepository = appZnsTemplateRepository;
        this.appZnsTemplateMapper = appZnsTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link AppZnsTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppZnsTemplateDTO> findByCriteria(AppZnsTemplateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppZnsTemplate> specification = createSpecification(criteria);
        return appZnsTemplateRepository.findAll(specification, page).map(appZnsTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppZnsTemplateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AppZnsTemplate> specification = createSpecification(criteria);
        return appZnsTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link AppZnsTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppZnsTemplate> createSpecification(AppZnsTemplateCriteria criteria) {
        Specification<AppZnsTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppZnsTemplate_.id));
            }
            if (criteria.getZnsAction() != null) {
                specification = specification.and(buildSpecification(criteria.getZnsAction(), AppZnsTemplate_.znsAction));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AppZnsTemplate_.name));
            }
            if (criteria.getTemplateId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemplateId(), AppZnsTemplate_.templateId));
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AppZnsTemplate_.dataSourceMappingType)
                );
            }
            if (criteria.getTemplateDataMapping() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTemplateDataMapping(), AppZnsTemplate_.templateDataMapping)
                );
            }
            if (criteria.getTargetUrls() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTargetUrls(), AppZnsTemplate_.targetUrls));
            }
            if (criteria.getThumbnailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThumbnailId(), root ->
                        root.join(AppZnsTemplate_.thumbnail, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AppZnsTemplate_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
