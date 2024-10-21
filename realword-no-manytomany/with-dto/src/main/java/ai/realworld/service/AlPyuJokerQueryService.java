package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPyuJoker;
import ai.realworld.repository.AlPyuJokerRepository;
import ai.realworld.service.criteria.AlPyuJokerCriteria;
import ai.realworld.service.dto.AlPyuJokerDTO;
import ai.realworld.service.mapper.AlPyuJokerMapper;
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
 * Service for executing complex queries for {@link AlPyuJoker} entities in the database.
 * The main input is a {@link AlPyuJokerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPyuJokerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPyuJokerQueryService extends QueryService<AlPyuJoker> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerQueryService.class);

    private final AlPyuJokerRepository alPyuJokerRepository;

    private final AlPyuJokerMapper alPyuJokerMapper;

    public AlPyuJokerQueryService(AlPyuJokerRepository alPyuJokerRepository, AlPyuJokerMapper alPyuJokerMapper) {
        this.alPyuJokerRepository = alPyuJokerRepository;
        this.alPyuJokerMapper = alPyuJokerMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPyuJokerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPyuJokerDTO> findByCriteria(AlPyuJokerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPyuJoker> specification = createSpecification(criteria);
        return alPyuJokerRepository.findAll(specification, page).map(alPyuJokerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPyuJokerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPyuJoker> specification = createSpecification(criteria);
        return alPyuJokerRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPyuJokerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPyuJoker> createSpecification(AlPyuJokerCriteria criteria) {
        Specification<AlPyuJoker> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlPyuJoker_.id));
            }
            if (criteria.getBookingNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingNo(), AlPyuJoker_.bookingNo));
            }
            if (criteria.getNoteHeitiga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNoteHeitiga(), AlPyuJoker_.noteHeitiga));
            }
            if (criteria.getPeriodType() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodType(), AlPyuJoker_.periodType));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), AlPyuJoker_.fromDate));
            }
            if (criteria.getToDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToDate(), AlPyuJoker_.toDate));
            }
            if (criteria.getCheckInDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckInDate(), AlPyuJoker_.checkInDate));
            }
            if (criteria.getCheckOutDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckOutDate(), AlPyuJoker_.checkOutDate));
            }
            if (criteria.getNumberOfAdults() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfAdults(), AlPyuJoker_.numberOfAdults));
            }
            if (criteria.getNumberOfPreschoolers() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNumberOfPreschoolers(), AlPyuJoker_.numberOfPreschoolers)
                );
            }
            if (criteria.getNumberOfChildren() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfChildren(), AlPyuJoker_.numberOfChildren));
            }
            if (criteria.getBookingPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBookingPrice(), AlPyuJoker_.bookingPrice));
            }
            if (criteria.getExtraFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExtraFee(), AlPyuJoker_.extraFee));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), AlPyuJoker_.totalPrice));
            }
            if (criteria.getBookingStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getBookingStatus(), AlPyuJoker_.bookingStatus));
            }
            if (criteria.getHistoryRefJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHistoryRefJason(), AlPyuJoker_.historyRefJason));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(AlPyuJoker_.customer, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getPersonInChargeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPersonInChargeId(), root ->
                        root.join(AlPyuJoker_.personInCharge, JoinType.LEFT).get(EdSheeran_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPyuJoker_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
