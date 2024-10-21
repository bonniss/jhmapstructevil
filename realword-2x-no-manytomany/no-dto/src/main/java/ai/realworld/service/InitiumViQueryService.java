package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.InitiumVi;
import ai.realworld.repository.InitiumViRepository;
import ai.realworld.service.criteria.InitiumViCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InitiumVi} entities in the database.
 * The main input is a {@link InitiumViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InitiumVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InitiumViQueryService extends QueryService<InitiumVi> {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumViQueryService.class);

    private final InitiumViRepository initiumViRepository;

    public InitiumViQueryService(InitiumViRepository initiumViRepository) {
        this.initiumViRepository = initiumViRepository;
    }

    /**
     * Return a {@link Page} of {@link InitiumVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InitiumVi> findByCriteria(InitiumViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InitiumVi> specification = createSpecification(criteria);
        return initiumViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InitiumViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InitiumVi> specification = createSpecification(criteria);
        return initiumViRepository.count(specification);
    }

    /**
     * Function to convert {@link InitiumViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InitiumVi> createSpecification(InitiumViCriteria criteria) {
        Specification<InitiumVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InitiumVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), InitiumVi_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), InitiumVi_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), InitiumVi_.description));
            }
            if (criteria.getIsJelloSupported() != null) {
                specification = specification.and(buildSpecification(criteria.getIsJelloSupported(), InitiumVi_.isJelloSupported));
            }
        }
        return specification;
    }
}
