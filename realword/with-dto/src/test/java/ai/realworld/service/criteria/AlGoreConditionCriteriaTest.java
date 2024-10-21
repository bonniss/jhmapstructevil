package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlGoreConditionCriteriaTest {

    @Test
    void newAlGoreConditionCriteriaHasAllFiltersNullTest() {
        var alGoreConditionCriteria = new AlGoreConditionCriteria();
        assertThat(alGoreConditionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alGoreConditionCriteriaFluentMethodsCreatesFiltersTest() {
        var alGoreConditionCriteria = new AlGoreConditionCriteria();

        setAllFilters(alGoreConditionCriteria);

        assertThat(alGoreConditionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alGoreConditionCriteriaCopyCreatesNullFilterTest() {
        var alGoreConditionCriteria = new AlGoreConditionCriteria();
        var copy = alGoreConditionCriteria.copy();

        assertThat(alGoreConditionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreConditionCriteria)
        );
    }

    @Test
    void alGoreConditionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alGoreConditionCriteria = new AlGoreConditionCriteria();
        setAllFilters(alGoreConditionCriteria);

        var copy = alGoreConditionCriteria.copy();

        assertThat(alGoreConditionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreConditionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alGoreConditionCriteria = new AlGoreConditionCriteria();

        assertThat(alGoreConditionCriteria).hasToString("AlGoreConditionCriteria{}");
    }

    private static void setAllFilters(AlGoreConditionCriteria alGoreConditionCriteria) {
        alGoreConditionCriteria.id();
        alGoreConditionCriteria.subjectType();
        alGoreConditionCriteria.subject();
        alGoreConditionCriteria.action();
        alGoreConditionCriteria.note();
        alGoreConditionCriteria.parentId();
        alGoreConditionCriteria.applicationId();
        alGoreConditionCriteria.distinct();
    }

    private static Condition<AlGoreConditionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSubjectType()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlGoreConditionCriteria> copyFiltersAre(
        AlGoreConditionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSubjectType(), copy.getSubjectType()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
