package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilViCriteriaTest {

    @Test
    void newAlPyuDjibrilViCriteriaHasAllFiltersNullTest() {
        var alPyuDjibrilViCriteria = new AlPyuDjibrilViCriteria();
        assertThat(alPyuDjibrilViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPyuDjibrilViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPyuDjibrilViCriteria = new AlPyuDjibrilViCriteria();

        setAllFilters(alPyuDjibrilViCriteria);

        assertThat(alPyuDjibrilViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPyuDjibrilViCriteriaCopyCreatesNullFilterTest() {
        var alPyuDjibrilViCriteria = new AlPyuDjibrilViCriteria();
        var copy = alPyuDjibrilViCriteria.copy();

        assertThat(alPyuDjibrilViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuDjibrilViCriteria)
        );
    }

    @Test
    void alPyuDjibrilViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPyuDjibrilViCriteria = new AlPyuDjibrilViCriteria();
        setAllFilters(alPyuDjibrilViCriteria);

        var copy = alPyuDjibrilViCriteria.copy();

        assertThat(alPyuDjibrilViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuDjibrilViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPyuDjibrilViCriteria = new AlPyuDjibrilViCriteria();

        assertThat(alPyuDjibrilViCriteria).hasToString("AlPyuDjibrilViCriteria{}");
    }

    private static void setAllFilters(AlPyuDjibrilViCriteria alPyuDjibrilViCriteria) {
        alPyuDjibrilViCriteria.id();
        alPyuDjibrilViCriteria.rateType();
        alPyuDjibrilViCriteria.rate();
        alPyuDjibrilViCriteria.isEnabled();
        alPyuDjibrilViCriteria.propertyId();
        alPyuDjibrilViCriteria.applicationId();
        alPyuDjibrilViCriteria.distinct();
    }

    private static Condition<AlPyuDjibrilViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRateType()) &&
                condition.apply(criteria.getRate()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getPropertyId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPyuDjibrilViCriteria> copyFiltersAre(
        AlPyuDjibrilViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRateType(), copy.getRateType()) &&
                condition.apply(criteria.getRate(), copy.getRate()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getPropertyId(), copy.getPropertyId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
