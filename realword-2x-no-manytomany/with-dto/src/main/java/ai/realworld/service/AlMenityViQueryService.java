package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlMenityVi;
import ai.realworld.repository.AlMenityViRepository;
import ai.realworld.service.criteria.AlMenityViCriteria;
import ai.realworld.service.dto.AlMenityViDTO;
import ai.realworld.service.mapper.AlMenityViMapper;
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
 * Service for executing complex queries for {@link AlMenityVi} entities in the database.
 * The main input is a {@link AlMenityViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlMenityViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlMenityViQueryService extends QueryService<AlMenityVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityViQueryService.class);

    private final AlMenityViRepository alMenityViRepository;

    private final AlMenityViMapper alMenityViMapper;

    public AlMenityViQueryService(AlMenityViRepository alMenityViRepository, AlMenityViMapper alMenityViMapper) {
        this.alMenityViRepository = alMenityViRepository;
        this.alMenityViMapper = alMenityViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlMenityViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlMenityViDTO> findByCriteria(AlMenityViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlMenityVi> specification = createSpecification(criteria);
        return alMenityViRepository.findAll(specification, page).map(alMenityViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlMenityViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlMenityVi> specification = createSpecification(criteria);
        return alMenityViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlMenityViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlMenityVi> createSpecification(AlMenityViCriteria criteria) {
        Specification<AlMenityVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlMenityVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlMenityVi_.name));
            }
            if (criteria.getIconSvg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIconSvg(), AlMenityVi_.iconSvg));
            }
            if (criteria.getPropertyType() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertyType(), AlMenityVi_.propertyType));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlMenityVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
