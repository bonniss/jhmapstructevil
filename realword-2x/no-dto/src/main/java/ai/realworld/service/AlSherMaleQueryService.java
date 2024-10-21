package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlSherMale;
import ai.realworld.repository.AlSherMaleRepository;
import ai.realworld.service.criteria.AlSherMaleCriteria;
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
 * Service for executing complex queries for {@link AlSherMale} entities in the database.
 * The main input is a {@link AlSherMaleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlSherMale} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlSherMaleQueryService extends QueryService<AlSherMale> {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleQueryService.class);

    private final AlSherMaleRepository alSherMaleRepository;

    public AlSherMaleQueryService(AlSherMaleRepository alSherMaleRepository) {
        this.alSherMaleRepository = alSherMaleRepository;
    }

    /**
     * Return a {@link Page} of {@link AlSherMale} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlSherMale> findByCriteria(AlSherMaleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlSherMale> specification = createSpecification(criteria);
        return alSherMaleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlSherMaleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlSherMale> specification = createSpecification(criteria);
        return alSherMaleRepository.count(specification);
    }

    /**
     * Function to convert {@link AlSherMaleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlSherMale> createSpecification(AlSherMaleCriteria criteria) {
        Specification<AlSherMale> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlSherMale_.id));
            }
            if (criteria.getDataSourceMappingType() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDataSourceMappingType(), AlSherMale_.dataSourceMappingType)
                );
            }
            if (criteria.getKeyword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeyword(), AlSherMale_.keyword));
            }
            if (criteria.getProperty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProperty(), AlSherMale_.property));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlSherMale_.title));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlSherMale_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
