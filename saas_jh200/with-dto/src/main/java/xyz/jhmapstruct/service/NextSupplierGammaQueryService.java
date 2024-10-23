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
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.repository.NextSupplierGammaRepository;
import xyz.jhmapstruct.service.criteria.NextSupplierGammaCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierGammaDTO;
import xyz.jhmapstruct.service.mapper.NextSupplierGammaMapper;

/**
 * Service for executing complex queries for {@link NextSupplierGamma} entities in the database.
 * The main input is a {@link NextSupplierGammaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextSupplierGammaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextSupplierGammaQueryService extends QueryService<NextSupplierGamma> {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierGammaQueryService.class);

    private final NextSupplierGammaRepository nextSupplierGammaRepository;

    private final NextSupplierGammaMapper nextSupplierGammaMapper;

    public NextSupplierGammaQueryService(
        NextSupplierGammaRepository nextSupplierGammaRepository,
        NextSupplierGammaMapper nextSupplierGammaMapper
    ) {
        this.nextSupplierGammaRepository = nextSupplierGammaRepository;
        this.nextSupplierGammaMapper = nextSupplierGammaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextSupplierGammaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextSupplierGammaDTO> findByCriteria(NextSupplierGammaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextSupplierGamma> specification = createSpecification(criteria);
        return nextSupplierGammaRepository
            .fetchBagRelationships(nextSupplierGammaRepository.findAll(specification, page))
            .map(nextSupplierGammaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextSupplierGammaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextSupplierGamma> specification = createSpecification(criteria);
        return nextSupplierGammaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextSupplierGammaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextSupplierGamma> createSpecification(NextSupplierGammaCriteria criteria) {
        Specification<NextSupplierGamma> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextSupplierGamma_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NextSupplierGamma_.name));
            }
            if (criteria.getContactPerson() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPerson(), NextSupplierGamma_.contactPerson));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextSupplierGamma_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextSupplierGamma_.phoneNumber));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextSupplierGamma_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
            if (criteria.getProductsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductsId(), root ->
                        root.join(NextSupplierGamma_.products, JoinType.LEFT).get(NextProductGamma_.id)
                    )
                );
            }
        }
        return specification;
    }
}
