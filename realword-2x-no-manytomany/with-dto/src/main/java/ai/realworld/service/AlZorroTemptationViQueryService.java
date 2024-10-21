package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlZorroTemptationVi;
import ai.realworld.repository.AlZorroTemptationViRepository;
import ai.realworld.service.criteria.AlZorroTemptationViCriteria;
import ai.realworld.service.dto.AlZorroTemptationViDTO;
import ai.realworld.service.mapper.AlZorroTemptationViMapper;
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
 * Service for executing complex queries for {@link AlZorroTemptationVi} entities in the database.
 * The main input is a {@link AlZorroTemptationViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlZorroTemptationViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlZorroTemptationViQueryService extends QueryService<AlZorroTemptationVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationViQueryService.class);

    private final AlZorroTemptationViRepository alZorroTemptationViRepository;

    private final AlZorroTemptationViMapper alZorroTemptationViMapper;

    public AlZorroTemptationViQueryService(
        AlZorroTemptationViRepository alZorroTemptationViRepository,
        AlZorroTemptationViMapper alZorroTemptationViMapper
    ) {
        this.alZorroTemptationViRepository = alZorroTemptationViRepository;
        this.alZorroTemptationViMapper = alZorroTemptationViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlZorroTemptationViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlZorroTemptationViDTO> findByCriteria(AlZorroTemptationViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlZorroTemptationVi> specification = createSpecification(criteria);
        return alZorroTemptationViRepository.findAll(specification, page).map(alZorroTemptationViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlZorroTemptationViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlZorroTemptationVi> specification = createSpecification(criteria);
        return alZorroTemptationViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlZorroTemptationViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlZorroTemptationVi> createSpecification(AlZorroTemptationViCriteria criteria) {
        Specification<AlZorroTemptationVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlZorroTemptationVi_.id));
            }
            if (criteria.getZipAction() != null) {
                specification = specification.and(buildSpecification(criteria.getZipAction(), AlZorroTemptationVi_.zipAction));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlZorroTemptationVi_.name));
            }
            if (criteria.getTemplateId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemplateId(), AlZorroTemptationVi_.templateId));
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AlZorroTemptationVi_.dataSourceMappingType)
                );
            }
            if (criteria.getTemplateDataMapping() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTemplateDataMapping(), AlZorroTemptationVi_.templateDataMapping)
                );
            }
            if (criteria.getTargetUrls() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTargetUrls(), AlZorroTemptationVi_.targetUrls));
            }
            if (criteria.getThumbnailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThumbnailId(), root ->
                        root.join(AlZorroTemptationVi_.thumbnail, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlZorroTemptationVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
