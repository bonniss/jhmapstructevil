package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlGoreConditionViCriteriaTest {

    @Test
    void newAlGoreConditionViCriteriaHasAllFiltersNullTest() {
        var alGoreConditionViCriteria = new AlGoreConditionViCriteria();
        assertThat(alGoreConditionViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alGoreConditionViCriteriaFluentMethodsCreatesFiltersTest() {
        var alGoreConditionViCriteria = new AlGoreConditionViCriteria();

        setAllFilters(alGoreConditionViCriteria);

        assertThat(alGoreConditionViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alGoreConditionViCriteriaCopyCreatesNullFilterTest() {
        var alGoreConditionViCriteria = new AlGoreConditionViCriteria();
        var copy = alGoreConditionViCriteria.copy();

        assertThat(alGoreConditionViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreConditionViCriteria)
        );
    }

    @Test
    void alGoreConditionViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alGoreConditionViCriteria = new AlGoreConditionViCriteria();
        setAllFilters(alGoreConditionViCriteria);

        var copy = alGoreConditionViCriteria.copy();

        assertThat(alGoreConditionViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alGoreConditionViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alGoreConditionViCriteria = new AlGoreConditionViCriteria();

        assertThat(alGoreConditionViCriteria).hasToString("AlGoreConditionViCriteria{}");
    }

    private static void setAllFilters(AlGoreConditionViCriteria alGoreConditionViCriteria) {
        alGoreConditionViCriteria.id();
        alGoreConditionViCriteria.subjectType();
        alGoreConditionViCriteria.subject();
        alGoreConditionViCriteria.action();
        alGoreConditionViCriteria.note();
        alGoreConditionViCriteria.parentId();
        alGoreConditionViCriteria.applicationId();
        alGoreConditionViCriteria.distinct();
    }

    private static Condition<AlGoreConditionViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlGoreConditionViCriteria> copyFiltersAre(
        AlGoreConditionViCriteria copy,
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
