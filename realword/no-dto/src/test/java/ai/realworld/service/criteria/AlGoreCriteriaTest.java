package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlGoreCriteriaTest {

    @Test
    void newAlGoreCriteriaHasAllFiltersNullTest() {
        var alGoreCriteria = new AlGoreCriteria();
        assertThat(alGoreCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alGoreCriteriaFluentMethodsCreatesFiltersTest() {
        var alGoreCriteria = new AlGoreCriteria();

        setAllFilters(alGoreCriteria);

        assertThat(alGoreCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alGoreCriteriaCopyCreatesNullFilterTest() {
        var alGoreCriteria = new AlGoreCriteria();
        var copy = alGoreCriteria.copy();

        assertThat(alGoreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreCriteria)
        );
    }

    @Test
    void alGoreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alGoreCriteria = new AlGoreCriteria();
        setAllFilters(alGoreCriteria);

        var copy = alGoreCriteria.copy();

        assertThat(alGoreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alGoreCriteria = new AlGoreCriteria();

        assertThat(alGoreCriteria).hasToString("AlGoreCriteria{}");
    }

    private static void setAllFilters(AlGoreCriteria alGoreCriteria) {
        alGoreCriteria.id();
        alGoreCriteria.name();
        alGoreCriteria.discountType();
        alGoreCriteria.discountRate();
        alGoreCriteria.scope();
        alGoreCriteria.bizRelationId();
        alGoreCriteria.applicationId();
        alGoreCriteria.conditionsId();
        alGoreCriteria.distinct();
    }

    private static Condition<AlGoreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDiscountType()) &&
                condition.apply(criteria.getDiscountRate()) &&
                condition.apply(criteria.getScope()) &&
                condition.apply(criteria.getBizRelationId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getConditionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlGoreCriteria> copyFiltersAre(AlGoreCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDiscountType(), copy.getDiscountType()) &&
                condition.apply(criteria.getDiscountRate(), copy.getDiscountRate()) &&
                condition.apply(criteria.getScope(), copy.getScope()) &&
                condition.apply(criteria.getBizRelationId(), copy.getBizRelationId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getConditionsId(), copy.getConditionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
