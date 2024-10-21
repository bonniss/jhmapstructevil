package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlVueVueViUsageCriteriaTest {

    @Test
    void newAlVueVueViUsageCriteriaHasAllFiltersNullTest() {
        var alVueVueViUsageCriteria = new AlVueVueViUsageCriteria();
        assertThat(alVueVueViUsageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alVueVueViUsageCriteriaFluentMethodsCreatesFiltersTest() {
        var alVueVueViUsageCriteria = new AlVueVueViUsageCriteria();

        setAllFilters(alVueVueViUsageCriteria);

        assertThat(alVueVueViUsageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alVueVueViUsageCriteriaCopyCreatesNullFilterTest() {
        var alVueVueViUsageCriteria = new AlVueVueViUsageCriteria();
        var copy = alVueVueViUsageCriteria.copy();

        assertThat(alVueVueViUsageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueViUsageCriteria)
        );
    }

    @Test
    void alVueVueViUsageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alVueVueViUsageCriteria = new AlVueVueViUsageCriteria();
        setAllFilters(alVueVueViUsageCriteria);

        var copy = alVueVueViUsageCriteria.copy();

        assertThat(alVueVueViUsageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueViUsageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alVueVueViUsageCriteria = new AlVueVueViUsageCriteria();

        assertThat(alVueVueViUsageCriteria).hasToString("AlVueVueViUsageCriteria{}");
    }

    private static void setAllFilters(AlVueVueViUsageCriteria alVueVueViUsageCriteria) {
        alVueVueViUsageCriteria.id();
        alVueVueViUsageCriteria.applicationId();
        alVueVueViUsageCriteria.voucherId();
        alVueVueViUsageCriteria.customerId();
        alVueVueViUsageCriteria.distinct();
    }

    private static Condition<AlVueVueViUsageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlVueVueViUsageCriteria> copyFiltersAre(
        AlVueVueViUsageCriteria copy,
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
