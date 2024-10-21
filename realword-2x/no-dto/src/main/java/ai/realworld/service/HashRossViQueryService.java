package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.HashRossVi;
import ai.realworld.repository.HashRossViRepository;
import ai.realworld.service.criteria.HashRossViCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HashRossVi} entities in the database.
 * The main input is a {@link HashRossViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HashRossVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HashRossViQueryService extends QueryService<HashRossVi> {

    private static final Logger LOG = LoggerFactory.getLogger(HashRossViQueryService.class);

    private final HashRossViRepository hashRossViRepository;

    public HashRossViQueryService(HashRossViRepository hashRossViRepository) {
        this.hashRossViRepository = hashRossViRepository;
    }

    /**
     * Return a {@link Page} of {@link HashRossVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HashRossVi> findByCriteria(HashRossViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HashRossVi> specification = createSpecification(criteria);
        return hashRossViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HashRossViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HashRossVi> specification = createSpecification(criteria);
        return hashRossViRepository.count(specification);
    }

    /**
     * Function to convert {@link HashRossViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HashRossVi> createSpecification(HashRossViCriteria criteria) {
        Specification<HashRossVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HashRossVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HashRossVi_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), HashRossVi_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HashRossVi_.description));
            }
            if (criteria.getPermissionGridJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPermissionGridJason(), HashRossVi_.permissionGridJason)
                );
            }
        }
        return specification;
    }
}
