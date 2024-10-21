package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlGoogleMeetVi;
import ai.realworld.repository.AlGoogleMeetViRepository;
import ai.realworld.service.criteria.AlGoogleMeetViCriteria;
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
 * Service for executing complex queries for {@link AlGoogleMeetVi} entities in the database.
 * The main input is a {@link AlGoogleMeetViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlGoogleMeetVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlGoogleMeetViQueryService extends QueryService<AlGoogleMeetVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetViQueryService.class);

    private final AlGoogleMeetViRepository alGoogleMeetViRepository;

    public AlGoogleMeetViQueryService(AlGoogleMeetViRepository alGoogleMeetViRepository) {
        this.alGoogleMeetViRepository = alGoogleMeetViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlGoogleMeetVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlGoogleMeetVi> findByCriteria(AlGoogleMeetViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlGoogleMeetVi> specification = createSpecification(criteria);
        return alGoogleMeetViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlGoogleMeetViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlGoogleMeetVi> specification = createSpecification(criteria);
        return alGoogleMeetViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlGoogleMeetViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlGoogleMeetVi> createSpecification(AlGoogleMeetViCriteria criteria) {
        Specification<AlGoogleMeetVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlGoogleMeetVi_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlGoogleMeetVi_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AlGoogleMeetVi_.description));
            }
            if (criteria.getNumberOfParticipants() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNumberOfParticipants(), AlGoogleMeetVi_.numberOfParticipants)
                );
            }
            if (criteria.getPlannedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlannedDate(), AlGoogleMeetVi_.plannedDate));
            }
            if (criteria.getPlannedDurationInMinutes() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getPlannedDurationInMinutes(), AlGoogleMeetVi_.plannedDurationInMinutes)
                );
            }
            if (criteria.getActualDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualDate(), AlGoogleMeetVi_.actualDate));
            }
            if (criteria.getActualDurationInMinutes() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getActualDurationInMinutes(), AlGoogleMeetVi_.actualDurationInMinutes)
                );
            }
            if (criteria.getContentJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentJason(), AlGoogleMeetVi_.contentJason));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlGoogleMeetVi_.customer, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(AlGoogleMeetVi_.agency, JoinType.LEFT).get(AlAppleVi_.id))
                );
            }
            if (criteria.getPersonInChargeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPersonInChargeId(), root ->
                        root.join(AlGoogleMeetVi_.personInCharge, JoinType.LEFT).get(EdSheeranVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlGoogleMeetVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
