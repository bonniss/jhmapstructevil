package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlActisoVi;
import ai.realworld.repository.AlActisoViRepository;
import ai.realworld.service.criteria.AlActisoViCriteria;
import ai.realworld.service.dto.AlActisoViDTO;
import ai.realworld.service.mapper.AlActisoViMapper;
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
 * Service for executing complex queries for {@link AlActisoVi} entities in the database.
 * The main input is a {@link AlActisoViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlActisoViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlActisoViQueryService extends QueryService<AlActisoVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoViQueryService.class);

    private final AlActisoViRepository alActisoViRepository;

    private final AlActisoViMapper alActisoViMapper;

    public AlActisoViQueryService(AlActisoViRepository alActisoViRepository, AlActisoViMapper alActisoViMapper) {
        this.alActisoViRepository = alActisoViRepository;
        this.alActisoViMapper = alActisoViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlActisoViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlActisoViDTO> findByCriteria(AlActisoViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlActisoVi> specification = createSpecification(criteria);
        return alActisoViRepository.findAll(specification, page).map(alActisoViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlActisoViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlActisoVi> specification = createSpecification(criteria);
        return alActisoViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlActisoViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlActisoVi> createSpecification(AlActisoViCriteria criteria) {
        Specification<AlActisoVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlActisoVi_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), AlActisoVi_.key));
            }
            if (criteria.getValueJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueJason(), AlActisoVi_.valueJason));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlActisoVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
