package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPyuDjibrilCriteriaTest {

    @Test
    void newAlPyuDjibrilCriteriaHasAllFiltersNullTest() {
        var alPyuDjibrilCriteria = new AlPyuDjibrilCriteria();
        assertThat(alPyuDjibrilCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPyuDjibrilCriteriaFluentMethodsCreatesFiltersTest() {
        var alPyuDjibrilCriteria = new AlPyuDjibrilCriteria();

        setAllFilters(alPyuDjibrilCriteria);

        assertThat(alPyuDjibrilCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPyuDjibrilCriteriaCopyCreatesNullFilterTest() {
        var alPyuDjibrilCriteria = new AlPyuDjibrilCriteria();
        var copy = alPyuDjibrilCriteria.copy();

        assertThat(alPyuDjibrilCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuDjibrilCriteria)
        );
    }

    @Test
    void alPyuDjibrilCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPyuDjibrilCriteria = new AlPyuDjibrilCriteria();
        setAllFilters(alPyuDjibrilCriteria);

        var copy = alPyuDjibrilCriteria.copy();

        assertThat(alPyuDjibrilCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPyuDjibrilCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPyuDjibrilCriteria = new AlPyuDjibrilCriteria();

        assertThat(alPyuDjibrilCriteria).hasToString("AlPyuDjibrilCriteria{}");
    }

    private static void setAllFilters(AlPyuDjibrilCriteria alPyuDjibrilCriteria) {
        alPyuDjibrilCriteria.id();
        alPyuDjibrilCriteria.rateType();
        alPyuDjibrilCriteria.rate();
        alPyuDjibrilCriteria.isEnabled();
        alPyuDjibrilCriteria.propertyId();
        alPyuDjibrilCriteria.applicationId();
        alPyuDjibrilCriteria.distinct();
    }

    private static Condition<AlPyuDjibrilCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPyuDjibrilCriteria> copyFiltersAre(
        AlPyuDjibrilCriteria copy,
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
