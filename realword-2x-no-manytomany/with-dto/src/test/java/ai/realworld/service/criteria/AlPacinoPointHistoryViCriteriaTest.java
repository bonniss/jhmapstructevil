package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryViCriteriaTest {

    @Test
    void newAlPacinoPointHistoryViCriteriaHasAllFiltersNullTest() {
        var alPacinoPointHistoryViCriteria = new AlPacinoPointHistoryViCriteria();
        assertThat(alPacinoPointHistoryViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPacinoPointHistoryViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPacinoPointHistoryViCriteria = new AlPacinoPointHistoryViCriteria();

        setAllFilters(alPacinoPointHistoryViCriteria);

        assertThat(alPacinoPointHistoryViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPacinoPointHistoryViCriteriaCopyCreatesNullFilterTest() {
        var alPacinoPointHistoryViCriteria = new AlPacinoPointHistoryViCriteria();
        var copy = alPacinoPointHistoryViCriteria.copy();

        assertThat(alPacinoPointHistoryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoPointHistoryViCriteria)
        );
    }

    @Test
    void alPacinoPointHistoryViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPacinoPointHistoryViCriteria = new AlPacinoPointHistoryViCriteria();
        setAllFilters(alPacinoPointHistoryViCriteria);

        var copy = alPacinoPointHistoryViCriteria.copy();

        assertThat(alPacinoPointHistoryViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPacinoPointHistoryViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPacinoPointHistoryViCriteria = new AlPacinoPointHistoryViCriteria();

        assertThat(alPacinoPointHistoryViCriteria).hasToString("AlPacinoPointHistoryViCriteria{}");
    }

    private static void setAllFilters(AlPacinoPointHistoryViCriteria alPacinoPointHistoryViCriteria) {
        alPacinoPointHistoryViCriteria.id();
        alPacinoPointHistoryViCriteria.source();
        alPacinoPointHistoryViCriteria.associatedId();
        alPacinoPointHistoryViCriteria.pointAmount();
        alPacinoPointHistoryViCriteria.customerId();
        alPacinoPointHistoryViCriteria.applicationId();
        alPacinoPointHistoryViCriteria.distinct();
    }

    private static Condition<AlPacinoPointHistoryViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPacinoPointHistoryViCriteria> copyFiltersAre(
        AlPacinoPointHistoryViCriteria copy,
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
