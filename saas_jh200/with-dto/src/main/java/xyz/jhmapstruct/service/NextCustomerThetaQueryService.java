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
import xyz.jhmapstruct.domain.NextCustomerTheta;
import xyz.jhmapstruct.repository.NextCustomerThetaRepository;
import xyz.jhmapstruct.service.criteria.NextCustomerThetaCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerThetaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerThetaMapper;

/**
 * Service for executing complex queries for {@link NextCustomerTheta} entities in the database.
 * The main input is a {@link NextCustomerThetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NextCustomerThetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NextCustomerThetaQueryService extends QueryService<NextCustomerTheta> {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerThetaQueryService.class);

    private final NextCustomerThetaRepository nextCustomerThetaRepository;

    private final NextCustomerThetaMapper nextCustomerThetaMapper;

    public NextCustomerThetaQueryService(
        NextCustomerThetaRepository nextCustomerThetaRepository,
        NextCustomerThetaMapper nextCustomerThetaMapper
    ) {
        this.nextCustomerThetaRepository = nextCustomerThetaRepository;
        this.nextCustomerThetaMapper = nextCustomerThetaMapper;
    }

    /**
     * Return a {@link Page} of {@link NextCustomerThetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NextCustomerThetaDTO> findByCriteria(NextCustomerThetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NextCustomerTheta> specification = createSpecification(criteria);
        return nextCustomerThetaRepository.findAll(specification, page).map(nextCustomerThetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NextCustomerThetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NextCustomerTheta> specification = createSpecification(criteria);
        return nextCustomerThetaRepository.count(specification);
    }

    /**
     * Function to convert {@link NextCustomerThetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NextCustomerTheta> createSpecification(NextCustomerThetaCriteria criteria) {
        Specification<NextCustomerTheta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NextCustomerTheta_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), NextCustomerTheta_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), NextCustomerTheta_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), NextCustomerTheta_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), NextCustomerTheta_.phoneNumber));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrdersId(), root ->
                        root.join(NextCustomerTheta_.orders, JoinType.LEFT).get(NextOrderTheta_.id)
                    )
                );
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTenantId(), root ->
                        root.join(NextCustomerTheta_.tenant, JoinType.LEFT).get(MasterTenant_.id)
                    )
                );
            }
        }
        return specification;
    }
}
