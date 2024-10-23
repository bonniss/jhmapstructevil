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
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.repository.NextPaymentViViRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentViViCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentViViDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentViViMapper;

/**
 * Service for executing complex queries for {@link NextPaymentViVi} entities in the database.
 * The main input is a {@link NextPaymentViViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentViViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentViViQueryService extends QueryService<NextPaymentViVi> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViViQueryService.class);

    private final NextPaymentViViRepository nextPaymentViViRepository;

    private final NextPaymentViViMapper nextPaymentViViMapper;

    public NextPaymentViViQueryService(NextPaymentViViRepository nextPaymentViViRepository, NextPaymentViViMapper nextPaymentViViMapper) {
        this.nextPaymentViViRepository = nextPaymentViViRepository;
        this.nextPaymentViViMapper = nextPaymentViViMapper;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentViViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentViViDTO> findByCriteria(NextPaymentViViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentViVi> specification = createSpecification(criteria);
        return nextPaymentViViRepository.findAll(specification, page).map(nextPaymentViViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentViViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentViVi> specification = createSpecification(criteria);
        return nextPaymentViViRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentViViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentViVi> createSpecification(NextPaymentViViCriteria criteria) {
        Specification<NextPaymentViVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentViVi_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentViVi_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentViVi_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentViVi_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentViVi_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
