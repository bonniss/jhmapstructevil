package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlBestTooth;
import ai.realworld.repository.AlBestToothRepository;
import ai.realworld.service.criteria.AlBestToothCriteria;
import ai.realworld.service.dto.AlBestToothDTO;
import ai.realworld.service.mapper.AlBestToothMapper;
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
 * Service for executing complex queries for {@link AlBestTooth} entities in the database.
 * The main input is a {@link AlBestToothCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlBestToothDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlBestToothQueryService extends QueryService<AlBestTooth> {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothQueryService.class);

    private final AlBestToothRepository alBestToothRepository;

    private final AlBestToothMapper alBestToothMapper;

    public AlBestToothQueryService(AlBestToothRepository alBestToothRepository, AlBestToothMapper alBestToothMapper) {
        this.alBestToothRepository = alBestToothRepository;
        this.alBestToothMapper = alBestToothMapper;
    }

    /**
     * Return a {@link Page} of {@link AlBestToothDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlBestToothDTO> findByCriteria(AlBestToothCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlBestTooth> specification = createSpecification(criteria);
        return alBestToothRepository.findAll(specification, page).map(alBestToothMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlBestToothCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlBestTooth> specification = createSpecification(criteria);
        return alBestToothRepository.count(specification);
    }

    /**
     * Function to convert {@link AlBestToothCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlBestTooth> createSpecification(AlBestToothCriteria criteria) {
        Specification<AlBestTooth> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlBestTooth_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlBestTooth_.name));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlBestTooth_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
