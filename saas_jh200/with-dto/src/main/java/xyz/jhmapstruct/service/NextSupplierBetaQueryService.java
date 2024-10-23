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
import xyz.jhmapstruct.domain.NextSupplierBeta;
import xyz.jhmapstruct.repository.NextSupplierBetaRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierBetaCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierBetaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierBetaMapper;

/**
 * Service for executing complex queries for {@link NextSupplierBeta} entities in the database.
 * The main input is a {@link NextSupplierBetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierBetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierBetaQueryService extends QueryService<NextSupplierBeta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierBetaQueryService.class);

    private final NextSupplierBetaRepository nextSupplierBetaRepository;

    private final NextSupplierBetaMapper nextSupplierBetaMapper;

    public NextSupplierBetaQueryService(
        NextSupplierBetaRepository nextSupplierBetaRepository,
        NextSupplierBetaMapper nextSupplierBetaMapper
    ) {
        this.nextSupplierBetaRepository = nextSupplierBetaRepository;
        this.nextSupplierBetaMapper = nextSupplierBetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierBetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierBetaDTO> findByCriteria(NextSupplierBetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierBeta> specification = createSpecification(criteria);
        return nextSupplierBetaRepository
            .fetchBagRelationships(nextSupplierBetaRepository.findAll(specification, page))
            .map(nextSupplierBetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierBetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierBeta> specification = createSpecification(criteria);
        return nextSupplierBetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierBetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierBeta> createSpecification(NextSupplierBetaCriteria criteria) {
        Specification<NextSupplierBeta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierBeta_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierBeta_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierBeta_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierBeta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierBeta_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierBeta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierBeta_.products, JoinType.LEFT).get(NextProductBeta_.id)
                    )
                );
            }
        }
        return specification;
    }
}
