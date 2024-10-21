package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlCatalina;
import ai.realworld.repository.AlCatalinaRepository;
import ai.realworld.service.criteria.AlCatalinaCriteria;
import ai.realworld.service.dto.AlCatalinaDTO;
import ai.realworld.service.mapper.AlCatalinaMapper;
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
 * Service for executing complex queries for {@link AlCatalina} entities in the database.
 * The main input is a {@link AlCatalinaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlCatalinaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlCatalinaQueryService extends QueryService<AlCatalina> {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaQueryService.class);

    private final AlCatalinaRepository alCatalinaRepository;

    private final AlCatalinaMapper alCatalinaMapper;

    public AlCatalinaQueryService(AlCatalinaRepository alCatalinaRepository, AlCatalinaMapper alCatalinaMapper) {
        this.alCatalinaRepository = alCatalinaRepository;
        this.alCatalinaMapper = alCatalinaMapper;
    }

    /**
     * Return a {@link Page} of {@link AlCatalinaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlCatalinaDTO> findByCriteria(AlCatalinaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlCatalina> specification = createSpecification(criteria);
        return alCatalinaRepository.findAll(specification, page).map(alCatalinaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlCatalinaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlCatalina> specification = createSpecification(criteria);
        return alCatalinaRepository.count(specification);
    }

    /**
     * Function to convert {@link AlCatalinaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlCatalina> createSpecification(AlCatalinaCriteria criteria) {
        Specification<AlCatalina> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlCatalina_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlCatalina_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlCatalina_.description));
            }
            if (criteria.getTreeDepth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTreeDepth(), AlCatalina_.treeDepth));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getParentId(), root -> root.join(AlCatalina_.parent, JoinType.LEFT).get(AlCatalina_.id))
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlCatalina_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlCatalina_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getChildrenId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getChildrenId(), root -> root.join(AlCatalina_.children, JoinType.LEFT).get(AlCatalina_.id))
                );
            }
        }
        return specification;
    }
}
