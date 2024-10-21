package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPyuDjibril;
import ai.realworld.repository.AlPyuDjibrilRepository;
import ai.realworld.service.criteria.AlPyuDjibrilCriteria;
import ai.realworld.service.dto.AlPyuDjibrilDTO;
import ai.realworld.service.mapper.AlPyuDjibrilMapper;
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
 * Service for executing complex queries for {@link AlPyuDjibril} entities in the database.
 * The main input is a {@link AlPyuDjibrilCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPyuDjibrilDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPyuDjibrilQueryService extends QueryService<AlPyuDjibril> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilQueryService.class);

    private final AlPyuDjibrilRepository alPyuDjibrilRepository;

    private final AlPyuDjibrilMapper alPyuDjibrilMapper;

    public AlPyuDjibrilQueryService(AlPyuDjibrilRepository alPyuDjibrilRepository, AlPyuDjibrilMapper alPyuDjibrilMapper) {
        this.alPyuDjibrilRepository = alPyuDjibrilRepository;
        this.alPyuDjibrilMapper = alPyuDjibrilMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPyuDjibrilDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPyuDjibrilDTO> findByCriteria(AlPyuDjibrilCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPyuDjibril> specification = createSpecification(criteria);
        return alPyuDjibrilRepository.findAll(specification, page).map(alPyuDjibrilMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPyuDjibrilCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPyuDjibril> specification = createSpecification(criteria);
        return alPyuDjibrilRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPyuDjibrilCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPyuDjibril> createSpecification(AlPyuDjibrilCriteria criteria) {
        Specification<AlPyuDjibril> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPyuDjibril_.id));
            }
            if (criteria.getRateType() != null) {
                specification = specification.and(buildSpecification(criteria.getRateType(), AlPyuDjibril_.rateType));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), AlPyuDjibril_.rate));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlPyuDjibril_.isEnabled));
            }
            if (criteria.getPropertyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPropertyId(), root -> root.join(AlPyuDjibril_.property, JoinType.LEFT).get(AlProty_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPyuDjibril_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
