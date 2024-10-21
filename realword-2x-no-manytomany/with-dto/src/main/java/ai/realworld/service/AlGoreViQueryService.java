package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlGoreVi;
import ai.realworld.repository.AlGoreViRepository;
import ai.realworld.service.criteria.AlGoreViCriteria;
import ai.realworld.service.dto.AlGoreViDTO;
import ai.realworld.service.mapper.AlGoreViMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlGoreVi} entities in the database.
 * The main input is a {@link AlGoreViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlGoreViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlGoreViQueryService extends QueryService<AlGoreVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreViQueryService.class);

    private final AlGoreViRepository alGoreViRepository;

    private final AlGoreViMapper alGoreViMapper;

    public AlGoreViQueryService(AlGoreViRepository alGoreViRepository, AlGoreViMapper alGoreViMapper) {
        this.alGoreViRepository = alGoreViRepository;
        this.alGoreViMapper = alGoreViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlGoreViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlGoreViDTO> findByCriteria(AlGoreViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlGoreVi> specification = createSpecification(criteria);
        return alGoreViRepository.findAll(specification, page).map(alGoreViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlGoreViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlGoreVi> specification = createSpecification(criteria);
        return alGoreViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlGoreViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlGoreVi> createSpecification(AlGoreViCriteria criteria) {
        Specification<AlGoreVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlGoreVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlGoreVi_.name));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), AlGoreVi_.discountType));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), AlGoreVi_.discountRate));
            }
            if (criteria.getScope() != null) {
                specification = specification.and(buildSpecification(criteria.getScope(), AlGoreVi_.scope));
            }
        }
        return specification;
    }
}
