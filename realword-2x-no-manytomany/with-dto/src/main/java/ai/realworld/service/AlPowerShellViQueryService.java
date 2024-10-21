package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPowerShellVi;
import ai.realworld.repository.AlPowerShellViRepository;
import ai.realworld.service.criteria.AlPowerShellViCriteria;
import ai.realworld.service.dto.AlPowerShellViDTO;
import ai.realworld.service.mapper.AlPowerShellViMapper;
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
 * Service for executing complex queries for {@link AlPowerShellVi} entities in the database.
 * The main input is a {@link AlPowerShellViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPowerShellViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPowerShellViQueryService extends QueryService<AlPowerShellVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellViQueryService.class);

    private final AlPowerShellViRepository alPowerShellViRepository;

    private final AlPowerShellViMapper alPowerShellViMapper;

    public AlPowerShellViQueryService(AlPowerShellViRepository alPowerShellViRepository, AlPowerShellViMapper alPowerShellViMapper) {
        this.alPowerShellViRepository = alPowerShellViRepository;
        this.alPowerShellViMapper = alPowerShellViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPowerShellViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPowerShellViDTO> findByCriteria(AlPowerShellViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPowerShellVi> specification = createSpecification(criteria);
        return alPowerShellViRepository.findAll(specification, page).map(alPowerShellViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPowerShellViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPowerShellVi> specification = createSpecification(criteria);
        return alPowerShellViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPowerShellViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPowerShellVi> createSpecification(AlPowerShellViCriteria criteria) {
        Specification<AlPowerShellVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPowerShellVi_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), AlPowerShellVi_.value));
            }
            if (criteria.getPropertyProfileId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPropertyProfileId(), root ->
                        root.join(AlPowerShellVi_.propertyProfile, JoinType.LEFT).get(AlProProVi_.id)
                    )
                );
            }
            if (criteria.getAttributeTermId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAttributeTermId(), root ->
                        root.join(AlPowerShellVi_.attributeTerm, JoinType.LEFT).get(AlPounderVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPowerShellVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
