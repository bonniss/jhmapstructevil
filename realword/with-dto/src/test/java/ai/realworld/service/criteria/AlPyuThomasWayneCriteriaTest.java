package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPyuThomasWayneCriteriaTest {

    @Test
    void newAlPyuThomasWayneCriteriaHasAllFiltersNullTest() {
        var alPyuThomasWayneCriteria = new AlPyuThomasWayneCriteria();
        assertThat(alPyuThomasWayneCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPyuThomasWayneCriteriaFluentMethodsCreatesFiltersTest() {
        var alPyuThomasWayneCriteria = new AlPyuThomasWayneCriteria();

        setAllFilters(alPyuThomasWayneCriteria);

        assertThat(alPyuThomasWayneCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPyuThomasWayneCriteriaCopyCreatesNullFilterTest() {
        var alPyuThomasWayneCriteria = new AlPyuThomasWayneCriteria();
        var copy = alPyuThomasWayneCriteria.copy();

        assertThat(alPyuThomasWayneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuThomasWayneCriteria)
        );
    }

    @Test
    void alPyuThomasWayneCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPyuThomasWayneCriteria = new AlPyuThomasWayneCriteria();
        setAllFilters(alPyuThomasWayneCriteria);

        var copy = alPyuThomasWayneCriteria.copy();

        assertThat(alPyuThomasWayneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuThomasWayneCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPyuThomasWayneCriteria = new AlPyuThomasWayneCriteria();

        assertThat(alPyuThomasWayneCriteria).hasToString("AlPyuThomasWayneCriteria{}");
    }

    private static void setAllFilters(AlPyuThomasWayneCriteria alPyuThomasWayneCriteria) {
        alPyuThomasWayneCriteria.id();
        alPyuThomasWayneCriteria.rating();
        alPyuThomasWayneCriteria.comment();
        alPyuThomasWayneCriteria.bookingId();
        alPyuThomasWayneCriteria.distinct();
    }

    private static Condition<AlPyuThomasWayneCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPyuThomasWayneCriteria> copyFiltersAre(
        AlPyuThomasWayneCriteria copy,
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
