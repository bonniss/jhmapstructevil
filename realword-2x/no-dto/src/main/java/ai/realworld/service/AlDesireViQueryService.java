package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlDesireVi;
import ai.realworld.repository.AlDesireViRepository;
import ai.realworld.service.criteria.AlDesireViCriteria;
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
 * Service for executing complex queries for {@link AlDesireVi} entities in the database.
 * The main input is a {@link AlDesireViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlDesireVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlDesireViQueryService extends QueryService<AlDesireVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireViQueryService.class);

    private final AlDesireViRepository alDesireViRepository;

    public AlDesireViQueryService(AlDesireViRepository alDesireViRepository) {
        this.alDesireViRepository = alDesireViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlDesireVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlDesireVi> findByCriteria(AlDesireViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlDesireVi> specification = createSpecification(criteria);
        return alDesireViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlDesireViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlDesireVi> specification = createSpecification(criteria);
        return alDesireViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlDesireViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlDesireVi> createSpecification(AlDesireViCriteria criteria) {
        Specification<AlDesireVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlDesireVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlDesireVi_.name));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlDesireVi_.weight));
            }
            if (criteria.getProbabilityOfWinning() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getProbabilityOfWinning(), AlDesireVi_.probabilityOfWinning)
                );
            }
            if (criteria.getMaximumWinningTime() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getMaximumWinningTime(), AlDesireVi_.maximumWinningTime)
                );
            }
            if (criteria.getIsWinningTimeLimited() != null) {
                specification = specification.and(buildSpecification(criteria.getIsWinningTimeLimited(), AlDesireVi_.isWinningTimeLimited));
            }
            if (criteria.getAwardResultType() != null) {
                specification = specification.and(buildSpecification(criteria.getAwardResultType(), AlDesireVi_.awardResultType));
            }
            if (criteria.getAwardReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAwardReference(), AlDesireVi_.awardReference));
            }
            if (criteria.getIsDefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefault(), AlDesireVi_.isDefault));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImageId(), root -> root.join(AlDesireVi_.image, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getMaggiId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaggiId(), root -> root.join(AlDesireVi_.maggi, JoinType.LEFT).get(AlLeandro_.id))
                );
            }
        }
        return specification;
    }
}
