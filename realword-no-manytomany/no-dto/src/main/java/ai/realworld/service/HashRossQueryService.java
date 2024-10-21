package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.HashRoss;
import ai.realworld.repository.HashRossRepository;
import ai.realworld.service.criteria.HashRossCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HashRoss} entities in the database.
 * The main input is a {@link HashRossCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HashRoss} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HashRossQueryService extends QueryService<HashRoss> {

    private static final Logger LOG = LoggerFactory.getLogger(HashRossQueryService.class);

    private final HashRossRepository hashRossRepository;

    public HashRossQueryService(HashRossRepository hashRossRepository) {
        this.hashRossRepository = hashRossRepository;
    }

    /**
     * Return a {@link Page} of {@link HashRoss} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HashRoss> findByCriteria(HashRossCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HashRoss> specification = createSpecification(criteria);
        return hashRossRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HashRossCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HashRoss> specification = createSpecification(criteria);
        return hashRossRepository.count(specification);
    }

    /**
     * Function to convert {@link HashRossCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HashRoss> createSpecification(HashRossCriteria criteria) {
        Specification<HashRoss> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HashRoss_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HashRoss_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), HashRoss_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HashRoss_.description));
            }
            if (criteria.getPermissionGridJason() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getPermissionGridJason(), HashRoss_.permissionGridJason)
                );
            }
        }
        return specification;
    }
}
