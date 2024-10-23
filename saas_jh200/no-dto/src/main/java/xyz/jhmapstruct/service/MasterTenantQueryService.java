package xyz.jhmapstruct.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import xyz.jhmapstruct.domain.*; // for static metamodels
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;
import xyz.jhmapstruct.service.criteria.MasterTenantCriteria;

/**
 * Service for executing complex queries for {@link MasterTenant} entities in the database.
 * The main input is a {@link MasterTenantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MasterTenant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MasterTenantQueryService extends QueryService<MasterTenant> {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantQueryService.class);

    private final MasterTenantRepository masterTenantRepository;

    public MasterTenantQueryService(MasterTenantRepository masterTenantRepository) {
        this.masterTenantRepository = masterTenantRepository;
    }

    /**
     * Return a {@link Page} of {@link MasterTenant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MasterTenant> findByCriteria(MasterTenantCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MasterTenant> specification = createSpecification(criteria);
        return masterTenantRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MasterTenantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MasterTenant> specification = createSpecification(criteria);
        return masterTenantRepository.count(specification);
    }

    /**
     * Function to convert {@link MasterTenantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MasterTenant> createSpecification(MasterTenantCriteria criteria) {
        Specification<MasterTenant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MasterTenant_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MasterTenant_.code));
            }
        }
        return specification;
    }
}
