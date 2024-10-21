package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLexFergVi;
import ai.realworld.repository.AlLexFergViRepository;
import ai.realworld.service.criteria.AlLexFergViCriteria;
import ai.realworld.service.dto.AlLexFergViDTO;
import ai.realworld.service.mapper.AlLexFergViMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlLexFergVi} entities in the database.
 * The main input is a {@link AlLexFergViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLexFergViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLexFergViQueryService extends QueryService<AlLexFergVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergViQueryService.class);

    private final AlLexFergViRepository alLexFergViRepository;

    private final AlLexFergViMapper alLexFergViMapper;

    public AlLexFergViQueryService(AlLexFergViRepository alLexFergViRepository, AlLexFergViMapper alLexFergViMapper) {
        this.alLexFergViRepository = alLexFergViRepository;
        this.alLexFergViMapper = alLexFergViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlLexFergViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLexFergViDTO> findByCriteria(AlLexFergViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLexFergVi> specification = createSpecification(criteria);
        return alLexFergViRepository.findAll(specification, page).map(alLexFergViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLexFergViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLexFergVi> specification = createSpecification(criteria);
        return alLexFergViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLexFergViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLexFergVi> createSpecification(AlLexFergViCriteria criteria) {
        Specification<AlLexFergVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlLexFergVi_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlLexFergVi_.title));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), AlLexFergVi_.slug));
            }
            if (criteria.getSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSummary(), AlLexFergVi_.summary));
            }
            if (criteria.getContentHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentHeitiga(), AlLexFergVi_.contentHeitiga));
            }
            if (criteria.getPublicationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPublicationStatus(), AlLexFergVi_.publicationStatus));
            }
            if (criteria.getPublishedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishedDate(), AlLexFergVi_.publishedDate));
            }
        }
        return specification;
    }
}
