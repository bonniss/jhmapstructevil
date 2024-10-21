package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.repository.OlAlmantinoMiloRepository;
import ai.realworld.service.criteria.OlAlmantinoMiloCriteria;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.service.mapper.OlAlmantinoMiloMapper;
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
 * Service for executing complex queries for {@link OlAlmantinoMilo} entities in the database.
 * The main input is a {@link OlAlmantinoMiloCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OlAlmantinoMiloDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OlAlmantinoMiloQueryService extends QueryService<OlAlmantinoMilo> {

    private static final Logger LOG = LoggerFactory.getLogger(OlAlmantinoMiloQueryService.class);

    private final OlAlmantinoMiloRepository olAlmantinoMiloRepository;

    private final OlAlmantinoMiloMapper olAlmantinoMiloMapper;

    public OlAlmantinoMiloQueryService(OlAlmantinoMiloRepository olAlmantinoMiloRepository, OlAlmantinoMiloMapper olAlmantinoMiloMapper) {
        this.olAlmantinoMiloRepository = olAlmantinoMiloRepository;
        this.olAlmantinoMiloMapper = olAlmantinoMiloMapper;
    }

    /**
     * Return a {@link Page} of {@link OlAlmantinoMiloDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OlAlmantinoMiloDTO> findByCriteria(OlAlmantinoMiloCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OlAlmantinoMilo> specification = createSpecification(criteria);
        return olAlmantinoMiloRepository.findAll(specification, page).map(olAlmantinoMiloMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OlAlmantinoMiloCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OlAlmantinoMilo> specification = createSpecification(criteria);
        return olAlmantinoMiloRepository.count(specification);
    }

    /**
     * Function to convert {@link OlAlmantinoMiloCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OlAlmantinoMilo> createSpecification(OlAlmantinoMiloCriteria criteria) {
        Specification<OlAlmantinoMilo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OlAlmantinoMilo_.id));
            }
            if (criteria.getProvider() != null) {
                specification = specification.and(buildSpecification(criteria.getProvider(), OlAlmantinoMilo_.provider));
            }
            if (criteria.getProviderAppManagerId() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getProviderAppManagerId(), OlAlmantinoMilo_.providerAppManagerId)
                );
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OlAlmantinoMilo_.name));
            }
            if (criteria.getProviderSecretKey() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getProviderSecretKey(), OlAlmantinoMilo_.providerSecretKey)
                );
            }
            if (criteria.getProviderToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProviderToken(), OlAlmantinoMilo_.providerToken));
            }
            if (criteria.getProviderRefreshToken() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getProviderRefreshToken(), OlAlmantinoMilo_.providerRefreshToken)
                );
            }
            if (criteria.getOrganizationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrganizationId(), root ->
                        root.join(OlAlmantinoMilo_.organization, JoinType.LEFT).get(OlMaster_.id)
                    )
                );
            }
            if (criteria.getApplicationsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationsId(), root ->
                        root.join(OlAlmantinoMilo_.applications, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
