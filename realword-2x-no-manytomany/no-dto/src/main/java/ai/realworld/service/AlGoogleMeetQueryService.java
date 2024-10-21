package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlGoogleMeet;
import ai.realworld.repository.AlGoogleMeetRepository;
import ai.realworld.service.criteria.AlGoogleMeetCriteria;
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
 * Service for executing complex queries for {@link AlGoogleMeet} entities in the database.
 * The main input is a {@link AlGoogleMeetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlGoogleMeet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlGoogleMeetQueryService extends QueryService<AlGoogleMeet> {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetQueryService.class);

    private final AlGoogleMeetRepository alGoogleMeetRepository;

    public AlGoogleMeetQueryService(AlGoogleMeetRepository alGoogleMeetRepository) {
        this.alGoogleMeetRepository = alGoogleMeetRepository;
    }

    /**
     * Return a {@link Page} of {@link AlGoogleMeet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlGoogleMeet> findByCriteria(AlGoogleMeetCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlGoogleMeet> specification = createSpecification(criteria);
        return alGoogleMeetRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlGoogleMeetCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlGoogleMeet> specification = createSpecification(criteria);
        return alGoogleMeetRepository.count(specification);
    }

    /**
     * Function to convert {@link AlGoogleMeetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlGoogleMeet> createSpecification(AlGoogleMeetCriteria criteria) {
        Specification<AlGoogleMeet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlGoogleMeet_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlGoogleMeet_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlGoogleMeet_.description));
            }
            if (criteria.getNumberOfParticipants() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNumberOfParticipants(), AlGoogleMeet_.numberOfParticipants)
                );
            }
            if (criteria.getPlannedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlannedDate(), AlGoogleMeet_.plannedDate));
            }
            if (criteria.getPlannedDurationInMinutes() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getPlannedDurationInMinutes(), AlGoogleMeet_.plannedDurationInMinutes)
                );
            }
            if (criteria.getActualDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualDate(), AlGoogleMeet_.actualDate));
            }
            if (criteria.getActualDurationInMinutes() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getActualDurationInMinutes(), AlGoogleMeet_.actualDurationInMinutes)
                );
            }
            if (criteria.getContentJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentJason(), AlGoogleMeet_.contentJason));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(AlGoogleMeet_.customer, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(AlGoogleMeet_.agency, JoinType.LEFT).get(AlApple_.id))
                );
            }
            if (criteria.getPersonInChargeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPersonInChargeId(), root ->
                        root.join(AlGoogleMeet_.personInCharge, JoinType.LEFT).get(EdSheeran_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlGoogleMeet_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
