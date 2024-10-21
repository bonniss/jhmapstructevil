package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlVueVueUsageCriteriaTest {

    @Test
    void newAlVueVueUsageCriteriaHasAllFiltersNullTest() {
        var alVueVueUsageCriteria = new AlVueVueUsageCriteria();
        assertThat(alVueVueUsageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alVueVueUsageCriteriaFluentMethodsCreatesFiltersTest() {
        var alVueVueUsageCriteria = new AlVueVueUsageCriteria();

        setAllFilters(alVueVueUsageCriteria);

        assertThat(alVueVueUsageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alVueVueUsageCriteriaCopyCreatesNullFilterTest() {
        var alVueVueUsageCriteria = new AlVueVueUsageCriteria();
        var copy = alVueVueUsageCriteria.copy();

        assertThat(alVueVueUsageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueUsageCriteria)
        );
    }

    @Test
    void alVueVueUsageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alVueVueUsageCriteria = new AlVueVueUsageCriteria();
        setAllFilters(alVueVueUsageCriteria);

        var copy = alVueVueUsageCriteria.copy();

        assertThat(alVueVueUsageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueUsageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alVueVueUsageCriteria = new AlVueVueUsageCriteria();

        assertThat(alVueVueUsageCriteria).hasToString("AlVueVueUsageCriteria{}");
    }

    private static void setAllFilters(AlVueVueUsageCriteria alVueVueUsageCriteria) {
        alVueVueUsageCriteria.id();
        alVueVueUsageCriteria.applicationId();
        alVueVueUsageCriteria.voucherId();
        alVueVueUsageCriteria.customerId();
        alVueVueUsageCriteria.distinct();
    }

    private static Condition<AlVueVueUsageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getVoucherId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlVueVueUsageCriteria> copyFiltersAre(
        AlVueVueUsageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getVoucherId(), copy.getVoucherId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
