package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.repository.AlPyuJokerViRepository;
import ai.realworld.service.criteria.AlPyuJokerViCriteria;
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
 * Service for executing complex queries for {@link AlPyuJokerVi} entities in the database.
 * The main input is a {@link AlPyuJokerViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPyuJokerVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPyuJokerViQueryService extends QueryService<AlPyuJokerVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerViQueryService.class);

    private final AlPyuJokerViRepository alPyuJokerViRepository;

    public AlPyuJokerViQueryService(AlPyuJokerViRepository alPyuJokerViRepository) {
        this.alPyuJokerViRepository = alPyuJokerViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPyuJokerVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPyuJokerVi> findByCriteria(AlPyuJokerViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPyuJokerVi> specification = createSpecification(criteria);
        return alPyuJokerViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPyuJokerViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPyuJokerVi> specification = createSpecification(criteria);
        return alPyuJokerViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPyuJokerViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPyuJokerVi> createSpecification(AlPyuJokerViCriteria criteria) {
        Specification<AlPyuJokerVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlPyuJokerVi_.id));
            }
            if (criteria.getBookingNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingNo(), AlPyuJokerVi_.bookingNo));
            }
            if (criteria.getNoteHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoteHeitiga(), AlPyuJokerVi_.noteHeitiga));
            }
            if (criteria.getPeriodType() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodType(), AlPyuJokerVi_.periodType));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), AlPyuJokerVi_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), AlPyuJokerVi_.toDate));
            }
            if (criteria.getCheckInDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckInDate(), AlPyuJokerVi_.checkInDate));
            }
            if (criteria.getCheckOutDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckOutDate(), AlPyuJokerVi_.checkOutDate));
            }
            if (criteria.getNumberOfAdults() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfAdults(), AlPyuJokerVi_.numberOfAdults));
            }
            if (criteria.getNumberOfPreschoolers() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNumberOfPreschoolers(), AlPyuJokerVi_.numberOfPreschoolers)
                );
            }
            if (criteria.getNumberOfChildren() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfChildren(), AlPyuJokerVi_.numberOfChildren));
            }
            if (criteria.getBookingPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBookingPrice(), AlPyuJokerVi_.bookingPrice));
            }
            if (criteria.getExtraFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExtraFee(), AlPyuJokerVi_.extraFee));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), AlPyuJokerVi_.totalPrice));
            }
            if (criteria.getBookingStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getBookingStatus(), AlPyuJokerVi_.bookingStatus));
            }
            if (criteria.getHistoryRefJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHistoryRefJason(), AlPyuJokerVi_.historyRefJason));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(AlPyuJokerVi_.customer, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getPersonInChargeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPersonInChargeId(), root ->
                        root.join(AlPyuJokerVi_.personInCharge, JoinType.LEFT).get(EdSheeranVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPyuJokerVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
