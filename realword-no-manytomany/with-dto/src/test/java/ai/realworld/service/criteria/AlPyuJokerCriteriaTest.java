package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPyuJokerCriteriaTest {

    @Test
    void newAlPyuJokerCriteriaHasAllFiltersNullTest() {
        var alPyuJokerCriteria = new AlPyuJokerCriteria();
        assertThat(alPyuJokerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPyuJokerCriteriaFluentMethodsCreatesFiltersTest() {
        var alPyuJokerCriteria = new AlPyuJokerCriteria();

        setAllFilters(alPyuJokerCriteria);

        assertThat(alPyuJokerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPyuJokerCriteriaCopyCreatesNullFilterTest() {
        var alPyuJokerCriteria = new AlPyuJokerCriteria();
        var copy = alPyuJokerCriteria.copy();

        assertThat(alPyuJokerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuJokerCriteria)
        );
    }

    @Test
    void alPyuJokerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPyuJokerCriteria = new AlPyuJokerCriteria();
        setAllFilters(alPyuJokerCriteria);

        var copy = alPyuJokerCriteria.copy();

        assertThat(alPyuJokerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuJokerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPyuJokerCriteria = new AlPyuJokerCriteria();

        assertThat(alPyuJokerCriteria).hasToString("AlPyuJokerCriteria{}");
    }

    private static void setAllFilters(AlPyuJokerCriteria alPyuJokerCriteria) {
        alPyuJokerCriteria.id();
        alPyuJokerCriteria.bookingNo();
        alPyuJokerCriteria.noteHeitiga();
        alPyuJokerCriteria.periodType();
        alPyuJokerCriteria.fromDate();
        alPyuJokerCriteria.toDate();
        alPyuJokerCriteria.checkInDate();
        alPyuJokerCriteria.checkOutDate();
        alPyuJokerCriteria.numberOfAdults();
        alPyuJokerCriteria.numberOfPreschoolers();
        alPyuJokerCriteria.numberOfChildren();
        alPyuJokerCriteria.bookingPrice();
        alPyuJokerCriteria.extraFee();
        alPyuJokerCriteria.totalPrice();
        alPyuJokerCriteria.bookingStatus();
        alPyuJokerCriteria.historyRefJason();
        alPyuJokerCriteria.customerId();
        alPyuJokerCriteria.personInChargeId();
        alPyuJokerCriteria.applicationId();
        alPyuJokerCriteria.distinct();
    }

    private static Condition<AlPyuJokerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBookingNo()) &&
                condition.apply(criteria.getNoteHeitiga()) &&
                condition.apply(criteria.getPeriodType()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getToDate()) &&
                condition.apply(criteria.getCheckInDate()) &&
                condition.apply(criteria.getCheckOutDate()) &&
                condition.apply(criteria.getNumberOfAdults()) &&
                condition.apply(criteria.getNumberOfPreschoolers()) &&
                condition.apply(criteria.getNumberOfChildren()) &&
                condition.apply(criteria.getBookingPrice()) &&
                condition.apply(criteria.getExtraFee()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getBookingStatus()) &&
                condition.apply(criteria.getHistoryRefJason()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getPersonInChargeId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPyuJokerCriteria> copyFiltersAre(AlPyuJokerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBookingNo(), copy.getBookingNo()) &&
                condition.apply(criteria.getNoteHeitiga(), copy.getNoteHeitiga()) &&
                condition.apply(criteria.getPeriodType(), copy.getPeriodType()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getToDate(), copy.getToDate()) &&
                condition.apply(criteria.getCheckInDate(), copy.getCheckInDate()) &&
                condition.apply(criteria.getCheckOutDate(), copy.getCheckOutDate()) &&
                condition.apply(criteria.getNumberOfAdults(), copy.getNumberOfAdults()) &&
                condition.apply(criteria.getNumberOfPreschoolers(), copy.getNumberOfPreschoolers()) &&
                condition.apply(criteria.getNumberOfChildren(), copy.getNumberOfChildren()) &&
                condition.apply(criteria.getBookingPrice(), copy.getBookingPrice()) &&
                condition.apply(criteria.getExtraFee(), copy.getExtraFee()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getBookingStatus(), copy.getBookingStatus()) &&
                condition.apply(criteria.getHistoryRefJason(), copy.getHistoryRefJason()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getPersonInChargeId(), copy.getPersonInChargeId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
