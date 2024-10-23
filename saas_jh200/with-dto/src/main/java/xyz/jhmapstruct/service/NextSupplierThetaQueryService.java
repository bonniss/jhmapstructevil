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
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.repository.NextSupplierThetaRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierThetaCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierThetaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierThetaMapper;

/**
 * Service for executing complex queries for {@link NextSupplierTheta} entities in the database.
 * The main input is a {@link NextSupplierThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierThetaQueryService extends QueryService<NextSupplierTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierThetaQueryService.class);

    private final NextSupplierThetaRepository nextSupplierThetaRepository;

    private final NextSupplierThetaMapper nextSupplierThetaMapper;

    public NextSupplierThetaQueryService(
        NextSupplierThetaRepository nextSupplierThetaRepository,
        NextSupplierThetaMapper nextSupplierThetaMapper
    ) {
        this.nextSupplierThetaRepository = nextSupplierThetaRepository;
        this.nextSupplierThetaMapper = nextSupplierThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierThetaDTO> findByCriteria(NextSupplierThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierTheta> specification = createSpecification(criteria);
        return nextSupplierThetaRepository
            .fetchBagRelationships(nextSupplierThetaRepository.findAll(specification, page))
            .map(nextSupplierThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierTheta> specification = createSpecification(criteria);
        return nextSupplierThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierTheta> createSpecification(NextSupplierThetaCriteria criteria) {
        Specification<NextSupplierTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierTheta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierTheta_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierTheta_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierTheta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierTheta_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierTheta_.products, JoinType.LEFT).get(NextProductTheta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
