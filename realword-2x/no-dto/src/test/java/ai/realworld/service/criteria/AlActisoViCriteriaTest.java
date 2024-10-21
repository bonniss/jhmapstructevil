package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlActisoViCriteriaTest {

    @Test
    void newAlActisoViCriteriaHasAllFiltersNullTest() {
        var alActisoViCriteria = new AlActisoViCriteria();
        assertThat(alActisoViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alActisoViCriteriaFluentMethodsCreatesFiltersTest() {
        var alActisoViCriteria = new AlActisoViCriteria();

        setAllFilters(alActisoViCriteria);

        assertThat(alActisoViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alActisoViCriteriaCopyCreatesNullFilterTest() {
        var alActisoViCriteria = new AlActisoViCriteria();
        var copy = alActisoViCriteria.copy();

        assertThat(alActisoViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alActisoViCriteria)
        );
    }

    @Test
    void alActisoViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alActisoViCriteria = new AlActisoViCriteria();
        setAllFilters(alActisoViCriteria);

        var copy = alActisoViCriteria.copy();

        assertThat(alActisoViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alActisoViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alActisoViCriteria = new AlActisoViCriteria();

        assertThat(alActisoViCriteria).hasToString("AlActisoViCriteria{}");
    }

    private static void setAllFilters(AlActisoViCriteria alActisoViCriteria) {
        alActisoViCriteria.id();
        alActisoViCriteria.key();
        alActisoViCriteria.valueJason();
        alActisoViCriteria.applicationId();
        alActisoViCriteria.distinct();
    }

    private static Condition<AlActisoViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getValueJason()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlActisoViCriteria> copyFiltersAre(AlActisoViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getValueJason(), copy.getValueJason()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
