package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPounderVi;
import ai.realworld.repository.AlPounderViRepository;
import ai.realworld.service.criteria.AlPounderViCriteria;
import ai.realworld.service.dto.AlPounderViDTO;
import ai.realworld.service.mapper.AlPounderViMapper;
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
 * Service for executing complex queries for {@link AlPounderVi} entities in the database.
 * The main input is a {@link AlPounderViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPounderViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPounderViQueryService extends QueryService<AlPounderVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderViQueryService.class);

    private final AlPounderViRepository alPounderViRepository;

    private final AlPounderViMapper alPounderViMapper;

    public AlPounderViQueryService(AlPounderViRepository alPounderViRepository, AlPounderViMapper alPounderViMapper) {
        this.alPounderViRepository = alPounderViRepository;
        this.alPounderViMapper = alPounderViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPounderViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPounderViDTO> findByCriteria(AlPounderViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPounderVi> specification = createSpecification(criteria);
        return alPounderViRepository.findAll(specification, page).map(alPounderViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPounderViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPounderVi> specification = createSpecification(criteria);
        return alPounderViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPounderViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPounderVi> createSpecification(AlPounderViCriteria criteria) {
        Specification<AlPounderVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPounderVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlPounderVi_.name));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AlPounderVi_.weight));
            }
            if (criteria.getAttributeTaxonomyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeTaxonomyId(), root ->
                        root.join(AlPounderVi_.attributeTaxonomy, JoinType.LEFT).get(AlPedroTaxVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPounderVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
