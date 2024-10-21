package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlZorroTemptation;
import ai.realworld.repository.AlZorroTemptationRepository;
import ai.realworld.service.criteria.AlZorroTemptationCriteria;
import ai.realworld.service.dto.AlZorroTemptationDTO;
import ai.realworld.service.mapper.AlZorroTemptationMapper;
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
 * Service for executing complex queries for {@link AlZorroTemptation} entities in the database.
 * The main input is a {@link AlZorroTemptationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlZorroTemptationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlZorroTemptationQueryService extends QueryService<AlZorroTemptation> {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationQueryService.class);

    private final AlZorroTemptationRepository alZorroTemptationRepository;

    private final AlZorroTemptationMapper alZorroTemptationMapper;

    public AlZorroTemptationQueryService(
        AlZorroTemptationRepository alZorroTemptationRepository,
        AlZorroTemptationMapper alZorroTemptationMapper
    ) {
        this.alZorroTemptationRepository = alZorroTemptationRepository;
        this.alZorroTemptationMapper = alZorroTemptationMapper;
    }

    /**
     * Return a {@link Page} of {@link AlZorroTemptationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlZorroTemptationDTO> findByCriteria(AlZorroTemptationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlZorroTemptation> specification = createSpecification(criteria);
        return alZorroTemptationRepository.findAll(specification, page).map(alZorroTemptationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlZorroTemptationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlZorroTemptation> specification = createSpecification(criteria);
        return alZorroTemptationRepository.count(specification);
    }

    /**
     * Function to convert {@link AlZorroTemptationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlZorroTemptation> createSpecification(AlZorroTemptationCriteria criteria) {
        Specification<AlZorroTemptation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlZorroTemptation_.id));
            }
            if (criteria.getZipAction() != null) {
                specification = specification.and(buildSpecification(criteria.getZipAction(), AlZorroTemptation_.zipAction));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlZorroTemptation_.name));
            }
            if (criteria.getTemplateId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemplateId(), AlZorroTemptation_.templateId));
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AlZorroTemptation_.dataSourceMappingType)
                );
            }
            if (criteria.getTemplateDataMapping() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getTemplateDataMapping(), AlZorroTemptation_.templateDataMapping)
                );
            }
            if (criteria.getTargetUrls() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTargetUrls(), AlZorroTemptation_.targetUrls));
            }
            if (criteria.getThumbnailId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getThumbnailId(), root ->
                        root.join(AlZorroTemptation_.thumbnail, JoinType.LEFT).get(Metaverse_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlZorroTemptation_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
