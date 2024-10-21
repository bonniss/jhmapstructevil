package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryCriteriaTest {

    @Test
    void newAlPacinoPointHistoryCriteriaHasAllFiltersNullTest() {
        var alPacinoPointHistoryCriteria = new AlPacinoPointHistoryCriteria();
        assertThat(alPacinoPointHistoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoPointHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoPointHistoryCriteria = new AlPacinoPointHistoryCriteria();

        setAllFilters(alPacinoPointHistoryCriteria);

        assertThat(alPacinoPointHistoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoPointHistoryCriteriaCopyCreatesNullFilterTest() {
        var alPacinoPointHistoryCriteria = new AlPacinoPointHistoryCriteria();
        var copy = alPacinoPointHistoryCriteria.copy();

        assertThat(alPacinoPointHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoPointHistoryCriteria)
        );
    }

    @Test
    void alPacinoPointHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoPointHistoryCriteria = new AlPacinoPointHistoryCriteria();
        setAllFilters(alPacinoPointHistoryCriteria);

        var copy = alPacinoPointHistoryCriteria.copy();

        assertThat(alPacinoPointHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoPointHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoPointHistoryCriteria = new AlPacinoPointHistoryCriteria();

        assertThat(alPacinoPointHistoryCriteria).hasToString("AlPacinoPointHistoryCriteria{}");
    }

    private static void setAllFilters(AlPacinoPointHistoryCriteria alPacinoPointHistoryCriteria) {
        alPacinoPointHistoryCriteria.id();
        alPacinoPointHistoryCriteria.source();
        alPacinoPointHistoryCriteria.associatedId();
        alPacinoPointHistoryCriteria.pointAmount();
        alPacinoPointHistoryCriteria.customerId();
        alPacinoPointHistoryCriteria.applicationId();
        alPacinoPointHistoryCriteria.distinct();
    }

    private static Condition<AlPacinoPointHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getAssociatedId()) &&
                condition.apply(criteria.getPointAmount()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPacinoPointHistoryCriteria> copyFiltersAre(
        AlPacinoPointHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getAssociatedId(), copy.getAssociatedId()) &&
                condition.apply(criteria.getPointAmount(), copy.getPointAmount()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
