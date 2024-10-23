package xyz.jhmapstruct.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import xyz.jhmapstruct.domain.*; // for static metamodels
import xyz.jhmapstruct.domain.NextProductTheta;
import xyz.jhmapstruct.repository.NextProductThetaRepository;
import xyz.jhmapstruct.service.criteria.NextProductThetaCriteria;
import xyz.jhmapstruct.service.dto.NextProductThetaDTO;
import xyz.jhmapstruct.service.mapper.NextProductThetaMapper;

/**
 * Service for executing complex queries for {@link NextProductTheta} entities in the database.
 * The main input is a {@link NextProductThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextProductThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextProductThetaQueryService extends QueryService<NextProductTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductThetaQueryService.class);

    private final NextProductThetaRepository nextProductThetaRepository;

    private final NextProductThetaMapper nextProductThetaMapper;

    public NextProductThetaQueryService(
        NextProductThetaRepository nextProductThetaRepository,
        NextProductThetaMapper nextProductThetaMapper
    ) {
        this.nextProductThetaRepository = nextProductThetaRepository;
        this.nextProductThetaMapper = nextProductThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextProductThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextProductThetaDTO> findByCriteria(NextProductThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextProductTheta> specification = createSpecification(criteria);
        return nextProductThetaRepository.findAll(specification, page).map(nextProductThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextProductThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextProductTheta> specification = createSpecification(criteria);
        return nextProductThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextProductThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextProductTheta> createSpecification(NextProductThetaCriteria criteria) {
        Specification<NextProductTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextProductTheta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextProductTheta_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), NextProductTheta_.price));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), NextProductTheta_.stock));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(NextProductTheta_.category, JoinType.LEFT).get(NextCategoryTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextProductTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root ->
                        root.join(NextProductTheta_.order, JoinType.LEFT).get(NextOrderTheta_.id)
                    )
                );
            }
            if (criteria.getSuppliersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSuppliersId(), root ->
                        root.join(NextProductTheta_.suppliers, JoinType.LEFT).get(NextSupplierTheta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
