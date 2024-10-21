package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacino;
import ai.realworld.repository.AlPacinoRepository;
import ai.realworld.service.criteria.AlPacinoCriteria;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.mapper.AlPacinoMapper;
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
 * Service for executing complex queries for {@link AlPacino} entities in the database.
 * The main input is a {@link AlPacinoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoQueryService extends QueryService<AlPacino> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoQueryService.class);

    private final AlPacinoRepository alPacinoRepository;

    private final AlPacinoMapper alPacinoMapper;

    public AlPacinoQueryService(AlPacinoRepository alPacinoRepository, AlPacinoMapper alPacinoMapper) {
        this.alPacinoRepository = alPacinoRepository;
        this.alPacinoMapper = alPacinoMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoDTO> findByCriteria(AlPacinoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacino> specification = createSpecification(criteria);
        return alPacinoRepository.findAll(specification, page).map(alPacinoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacino> specification = createSpecification(criteria);
        return alPacinoRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacino> createSpecification(AlPacinoCriteria criteria) {
        Specification<AlPacino> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlPacino_.id));
            }
            if (criteria.getPlatformCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlatformCode(), AlPacino_.platformCode));
            }
            if (criteria.getPlatformUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlatformUsername(), AlPacino_.platformUsername));
            }
            if (criteria.getPlatformAvatarUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlatformAvatarUrl(), AlPacino_.platformAvatarUrl));
            }
            if (criteria.getIsSensitive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSensitive(), AlPacino_.isSensitive));
            }
            if (criteria.getFamilyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFamilyName(), AlPacino_.familyName));
            }
            if (criteria.getGivenName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGivenName(), AlPacino_.givenName));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), AlPacino_.dob));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), AlPacino_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), AlPacino_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), AlPacino_.email));
            }
            if (criteria.getAcquiredFrom() != null) {
                specification = specification.and(buildSpecification(criteria.getAcquiredFrom(), AlPacino_.acquiredFrom));
            }
            if (criteria.getCurrentPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentPoints(), AlPacino_.currentPoints));
            }
            if (criteria.getTotalPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPoints(), AlPacino_.totalPoints));
            }
            if (criteria.getIsFollowing() != null) {
                specification = specification.and(buildSpecification(criteria.getIsFollowing(), AlPacino_.isFollowing));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), AlPacino_.isEnabled));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPacino_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getMembershipTierId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMembershipTierId(), root ->
                        root.join(AlPacino_.membershipTier, JoinType.LEFT).get(AlMemTier_.id)
                    )
                );
            }
            if (criteria.getAlVueVueUsageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAlVueVueUsageId(), root ->
                        root.join(AlPacino_.alVueVueUsage, JoinType.LEFT).get(AlVueVueUsage_.id)
                    )
                );
            }
            if (criteria.getMembershipTierViId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMembershipTierViId(), root ->
                        root.join(AlPacino_.membershipTierVi, JoinType.LEFT).get(AlMemTierVi_.id)
                    )
                );
            }
            if (criteria.getAlVueVueViUsageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAlVueVueViUsageId(), root ->
                        root.join(AlPacino_.alVueVueViUsage, JoinType.LEFT).get(AlVueVueViUsage_.id)
                    )
                );
            }
        }
        return specification;
    }
}
