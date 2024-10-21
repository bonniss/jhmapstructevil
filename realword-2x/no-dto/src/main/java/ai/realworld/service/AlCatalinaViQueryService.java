package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.repository.AlCatalinaViRepository;
import ai.realworld.service.criteria.AlCatalinaViCriteria;
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
 * Service for executing complex queries for {@link AlCatalinaVi} entities in the database.
 * The main input is a {@link AlCatalinaViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlCatalinaVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlCatalinaViQueryService extends QueryService<AlCatalinaVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaViQueryService.class);

    private final AlCatalinaViRepository alCatalinaViRepository;

    public AlCatalinaViQueryService(AlCatalinaViRepository alCatalinaViRepository) {
        this.alCatalinaViRepository = alCatalinaViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlCatalinaVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlCatalinaVi> findByCriteria(AlCatalinaViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlCatalinaVi> specification = createSpecification(criteria);
        return alCatalinaViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlCatalinaViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlCatalinaVi> specification = createSpecification(criteria);
        return alCatalinaViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlCatalinaViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlCatalinaVi> createSpecification(AlCatalinaViCriteria criteria) {
        Specification<AlCatalinaVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlCatalinaVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlCatalinaVi_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlCatalinaVi_.description));
            }
            if (criteria.getTreeDepth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTreeDepth(), AlCatalinaVi_.treeDepth));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlCatalinaVi_.parent, JoinType.LEFT).get(AlCatalinaVi_.id))
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlCatalinaVi_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root ->
                        root.join(AlCatalinaVi_.children, JoinType.LEFT).get(AlCatalinaVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
