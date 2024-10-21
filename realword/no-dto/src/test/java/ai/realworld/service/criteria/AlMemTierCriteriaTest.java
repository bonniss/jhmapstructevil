package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlMemTierCriteriaTest {

    @Test
    void newAlMemTierCriteriaHasAllFiltersNullTest() {
        var alMemTierCriteria = new AlMemTierCriteria();
        assertThat(alMemTierCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alMemTierCriteriaFluentMethodsCreatesFiltersTest() {
        var alMemTierCriteria = new AlMemTierCriteria();

        setAllFilters(alMemTierCriteria);

        assertThat(alMemTierCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alMemTierCriteriaCopyCreatesNullFilterTest() {
        var alMemTierCriteria = new AlMemTierCriteria();
        var copy = alMemTierCriteria.copy();

        assertThat(alMemTierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alMemTierCriteria)
        );
    }

    @Test
    void alMemTierCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alMemTierCriteria = new AlMemTierCriteria();
        setAllFilters(alMemTierCriteria);

        var copy = alMemTierCriteria.copy();

        assertThat(alMemTierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alMemTierCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alMemTierCriteria = new AlMemTierCriteria();

        assertThat(alMemTierCriteria).hasToString("AlMemTierCriteria{}");
    }

    private static void setAllFilters(AlMemTierCriteria alMemTierCriteria) {
        alMemTierCriteria.id();
        alMemTierCriteria.name();
        alMemTierCriteria.description();
        alMemTierCriteria.minPoint();
        alMemTierCriteria.applicationId();
        alMemTierCriteria.distinct();
    }

    private static Condition<AlMemTierCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlMemTierCriteria> copyFiltersAre(AlMemTierCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
