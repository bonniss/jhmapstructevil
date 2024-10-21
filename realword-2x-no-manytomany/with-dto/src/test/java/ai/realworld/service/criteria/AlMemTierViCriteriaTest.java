package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlMemTierViCriteriaTest {

    @Test
    void newAlMemTierViCriteriaHasAllFiltersNullTest() {
        var alMemTierViCriteria = new AlMemTierViCriteria();
        assertThat(alMemTierViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alMemTierViCriteriaFluentMethodsCreatesFiltersTest() {
        var alMemTierViCriteria = new AlMemTierViCriteria();

        setAllFilters(alMemTierViCriteria);

        assertThat(alMemTierViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alMemTierViCriteriaCopyCreatesNullFilterTest() {
        var alMemTierViCriteria = new AlMemTierViCriteria();
        var copy = alMemTierViCriteria.copy();

        assertThat(alMemTierViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alMemTierViCriteria)
        );
    }

    @Test
    void alMemTierViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alMemTierViCriteria = new AlMemTierViCriteria();
        setAllFilters(alMemTierViCriteria);

        var copy = alMemTierViCriteria.copy();

        assertThat(alMemTierViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alMemTierViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alMemTierViCriteria = new AlMemTierViCriteria();

        assertThat(alMemTierViCriteria).hasToString("AlMemTierViCriteria{}");
    }

    private static void setAllFilters(AlMemTierViCriteria alMemTierViCriteria) {
        alMemTierViCriteria.id();
        alMemTierViCriteria.name();
        alMemTierViCriteria.description();
        alMemTierViCriteria.minPoint();
        alMemTierViCriteria.applicationId();
        alMemTierViCriteria.distinct();
    }

    private static Condition<AlMemTierViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getMinPoint()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlMemTierViCriteria> copyFiltersAre(AlMemTierViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getMinPoint(), copy.getMinPoint()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
