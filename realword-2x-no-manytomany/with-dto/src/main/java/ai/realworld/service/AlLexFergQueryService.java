package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLexFerg;
import ai.realworld.repository.AlLexFergRepository;
import ai.realworld.service.criteria.AlLexFergCriteria;
import ai.realworld.service.dto.AlLexFergDTO;
import ai.realworld.service.mapper.AlLexFergMapper;
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
 * Service for executing complex queries for {@link AlLexFerg} entities in the database.
 * The main input is a {@link AlLexFergCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLexFergDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLexFergQueryService extends QueryService<AlLexFerg> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergQueryService.class);

    private final AlLexFergRepository alLexFergRepository;

    private final AlLexFergMapper alLexFergMapper;

    public AlLexFergQueryService(AlLexFergRepository alLexFergRepository, AlLexFergMapper alLexFergMapper) {
        this.alLexFergRepository = alLexFergRepository;
        this.alLexFergMapper = alLexFergMapper;
    }

    /**
     * Return a {@link Page} of {@link AlLexFergDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLexFergDTO> findByCriteria(AlLexFergCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLexFerg> specification = createSpecification(criteria);
        return alLexFergRepository.findAll(specification, page).map(alLexFergMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLexFergCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLexFerg> specification = createSpecification(criteria);
        return alLexFergRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLexFergCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLexFerg> createSpecification(AlLexFergCriteria criteria) {
        Specification<AlLexFerg> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlLexFerg_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlLexFerg_.title));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), AlLexFerg_.slug));
            }
            if (criteria.getSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSummary(), AlLexFerg_.summary));
            }
            if (criteria.getContentHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentHeitiga(), AlLexFerg_.contentHeitiga));
            }
            if (criteria.getPublicationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPublicationStatus(), AlLexFerg_.publicationStatus));
            }
            if (criteria.getPublishedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPublishedDate(), AlLexFerg_.publishedDate));
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlLexFerg_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(AlLexFerg_.category, JoinType.LEFT).get(AlCatalina_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlLexFerg_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
