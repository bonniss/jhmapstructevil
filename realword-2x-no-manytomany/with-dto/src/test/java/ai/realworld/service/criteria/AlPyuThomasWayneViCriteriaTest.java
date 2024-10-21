package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneViCriteriaTest {

    @Test
    void newAlPyuThomasWayneViCriteriaHasAllFiltersNullTest() {
        var alPyuThomasWayneViCriteria = new AlPyuThomasWayneViCriteria();
        assertThat(alPyuThomasWayneViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPyuThomasWayneViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPyuThomasWayneViCriteria = new AlPyuThomasWayneViCriteria();

        setAllFilters(alPyuThomasWayneViCriteria);

        assertThat(alPyuThomasWayneViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPyuThomasWayneViCriteriaCopyCreatesNullFilterTest() {
        var alPyuThomasWayneViCriteria = new AlPyuThomasWayneViCriteria();
        var copy = alPyuThomasWayneViCriteria.copy();

        assertThat(alPyuThomasWayneViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuThomasWayneViCriteria)
        );
    }

    @Test
    void alPyuThomasWayneViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPyuThomasWayneViCriteria = new AlPyuThomasWayneViCriteria();
        setAllFilters(alPyuThomasWayneViCriteria);

        var copy = alPyuThomasWayneViCriteria.copy();

        assertThat(alPyuThomasWayneViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuThomasWayneViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPyuThomasWayneViCriteria = new AlPyuThomasWayneViCriteria();

        assertThat(alPyuThomasWayneViCriteria).hasToString("AlPyuThomasWayneViCriteria{}");
    }

    private static void setAllFilters(AlPyuThomasWayneViCriteria alPyuThomasWayneViCriteria) {
        alPyuThomasWayneViCriteria.id();
        alPyuThomasWayneViCriteria.rating();
        alPyuThomasWayneViCriteria.comment();
        alPyuThomasWayneViCriteria.bookingId();
        alPyuThomasWayneViCriteria.distinct();
    }

    private static Condition<AlPyuThomasWayneViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRating()) &&
                condition.apply(criteria.getComment()) &&
                condition.apply(criteria.getBookingId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPyuThomasWayneViCriteria> copyFiltersAre(
        AlPyuThomasWayneViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRating(), copy.getRating()) &&
                condition.apply(criteria.getComment(), copy.getComment()) &&
                condition.apply(criteria.getBookingId(), copy.getBookingId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
