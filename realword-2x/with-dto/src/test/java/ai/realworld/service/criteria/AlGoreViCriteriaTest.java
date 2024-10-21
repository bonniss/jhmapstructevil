package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlGoreViCriteriaTest {

    @Test
    void newAlGoreViCriteriaHasAllFiltersNullTest() {
        var alGoreViCriteria = new AlGoreViCriteria();
        assertThat(alGoreViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alGoreViCriteriaFluentMethodsCreatesFiltersTest() {
        var alGoreViCriteria = new AlGoreViCriteria();

        setAllFilters(alGoreViCriteria);

        assertThat(alGoreViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alGoreViCriteriaCopyCreatesNullFilterTest() {
        var alGoreViCriteria = new AlGoreViCriteria();
        var copy = alGoreViCriteria.copy();

        assertThat(alGoreViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreViCriteria)
        );
    }

    @Test
    void alGoreViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alGoreViCriteria = new AlGoreViCriteria();
        setAllFilters(alGoreViCriteria);

        var copy = alGoreViCriteria.copy();

        assertThat(alGoreViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alGoreViCriteria = new AlGoreViCriteria();

        assertThat(alGoreViCriteria).hasToString("AlGoreViCriteria{}");
    }

    private static void setAllFilters(AlGoreViCriteria alGoreViCriteria) {
        alGoreViCriteria.id();
        alGoreViCriteria.name();
        alGoreViCriteria.discountType();
        alGoreViCriteria.discountRate();
        alGoreViCriteria.scope();
        alGoreViCriteria.distinct();
    }

    private static Condition<AlGoreViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDiscountType()) &&
                condition.apply(criteria.getDiscountRate()) &&
                condition.apply(criteria.getScope()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlGoreViCriteria> copyFiltersAre(AlGoreViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDiscountType(), copy.getDiscountType()) &&
                condition.apply(criteria.getDiscountRate(), copy.getDiscountRate()) &&
                condition.apply(criteria.getScope(), copy.getScope()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
