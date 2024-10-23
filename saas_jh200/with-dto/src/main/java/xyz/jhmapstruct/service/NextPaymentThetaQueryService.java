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
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.repository.NextPaymentThetaRepository;
import xyz.jhmapstruct.service.criteria.NextPaymentThetaCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentThetaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentThetaMapper;

/**
 * Service for executing complex queries for {@link NextPaymentTheta} entities in the database.
 * The main input is a {@link NextPaymentThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextPaymentThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextPaymentThetaQueryService extends QueryService<NextPaymentTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentThetaQueryService.class);

    private final NextPaymentThetaRepository nextPaymentThetaRepository;

    private final NextPaymentThetaMapper nextPaymentThetaMapper;

    public NextPaymentThetaQueryService(
        NextPaymentThetaRepository nextPaymentThetaRepository,
        NextPaymentThetaMapper nextPaymentThetaMapper
    ) {
        this.nextPaymentThetaRepository = nextPaymentThetaRepository;
        this.nextPaymentThetaMapper = nextPaymentThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextPaymentThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextPaymentThetaDTO> findByCriteria(NextPaymentThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextPaymentTheta> specification = createSpecification(criteria);
        return nextPaymentThetaRepository.findAll(specification, page).map(nextPaymentThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextPaymentThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextPaymentTheta> specification = createSpecification(criteria);
        return nextPaymentThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextPaymentThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextPaymentTheta> createSpecification(NextPaymentThetaCriteria criteria) {
        Specification<NextPaymentTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextPaymentTheta_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), NextPaymentTheta_.amount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), NextPaymentTheta_.paymentDate));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), NextPaymentTheta_.paymentMethod));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextPaymentTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
