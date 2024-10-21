package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlVueVueConditionCriteriaTest {

    @Test
    void newAlVueVueConditionCriteriaHasAllFiltersNullTest() {
        var alVueVueConditionCriteria = new AlVueVueConditionCriteria();
        assertThat(alVueVueConditionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alVueVueConditionCriteriaFluentMethodsCreatesFiltersTest() {
        var alVueVueConditionCriteria = new AlVueVueConditionCriteria();

        setAllFilters(alVueVueConditionCriteria);

        assertThat(alVueVueConditionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alVueVueConditionCriteriaCopyCreatesNullFilterTest() {
        var alVueVueConditionCriteria = new AlVueVueConditionCriteria();
        var copy = alVueVueConditionCriteria.copy();

        assertThat(alVueVueConditionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueConditionCriteria)
        );
    }

    @Test
    void alVueVueConditionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alVueVueConditionCriteria = new AlVueVueConditionCriteria();
        setAllFilters(alVueVueConditionCriteria);

        var copy = alVueVueConditionCriteria.copy();

        assertThat(alVueVueConditionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueConditionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alVueVueConditionCriteria = new AlVueVueConditionCriteria();

        assertThat(alVueVueConditionCriteria).hasToString("AlVueVueConditionCriteria{}");
    }

    private static void setAllFilters(AlVueVueConditionCriteria alVueVueConditionCriteria) {
        alVueVueConditionCriteria.id();
        alVueVueConditionCriteria.subjectType();
        alVueVueConditionCriteria.subject();
        alVueVueConditionCriteria.action();
        alVueVueConditionCriteria.note();
        alVueVueConditionCriteria.parentId();
        alVueVueConditionCriteria.applicationId();
        alVueVueConditionCriteria.distinct();
    }

    private static Condition<AlVueVueConditionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlVueVueConditionCriteria> copyFiltersAre(
        AlVueVueConditionCriteria copy,
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
