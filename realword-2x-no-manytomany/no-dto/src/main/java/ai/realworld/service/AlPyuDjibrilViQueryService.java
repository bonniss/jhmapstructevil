package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPyuDjibrilVi;
import ai.realworld.repository.AlPyuDjibrilViRepository;
import ai.realworld.service.criteria.AlPyuDjibrilViCriteria;
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
 * Service for executing complex queries for {@link AlPyuDjibrilVi} entities in the database.
 * The main input is a {@link AlPyuDjibrilViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPyuDjibrilVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPyuDjibrilViQueryService extends QueryService<AlPyuDjibrilVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilViQueryService.class);

    private final AlPyuDjibrilViRepository alPyuDjibrilViRepository;

    public AlPyuDjibrilViQueryService(AlPyuDjibrilViRepository alPyuDjibrilViRepository) {
        this.alPyuDjibrilViRepository = alPyuDjibrilViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPyuDjibrilVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPyuDjibrilVi> findByCriteria(AlPyuDjibrilViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPyuDjibrilVi> specification = createSpecification(criteria);
        return alPyuDjibrilViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPyuDjibrilViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPyuDjibrilVi> specification = createSpecification(criteria);
        return alPyuDjibrilViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPyuDjibrilViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPyuDjibrilVi> createSpecification(AlPyuDjibrilViCriteria criteria) {
        Specification<AlPyuDjibrilVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPyuDjibrilVi_.id));
            }
            if (criteria.getRateType() != null) {
                specification = specification.and(buildSpecification(criteria.getRateType(), AlPyuDjibrilVi_.rateType));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), AlPyuDjibrilVi_.rate));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlPyuDjibrilVi_.isEnabled));
            }
            if (criteria.getPropertyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPropertyId(), root ->
                        root.join(AlPyuDjibrilVi_.property, JoinType.LEFT).get(AlProtyVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPyuDjibrilVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
