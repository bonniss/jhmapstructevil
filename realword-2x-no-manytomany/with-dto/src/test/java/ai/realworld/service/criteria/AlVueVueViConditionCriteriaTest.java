package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlVueVueViConditionCriteriaTest {

    @Test
    void newAlVueVueViConditionCriteriaHasAllFiltersNullTest() {
        var alVueVueViConditionCriteria = new AlVueVueViConditionCriteria();
        assertThat(alVueVueViConditionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alVueVueViConditionCriteriaFluentMethodsCreatesFiltersTest() {
        var alVueVueViConditionCriteria = new AlVueVueViConditionCriteria();

        setAllFilters(alVueVueViConditionCriteria);

        assertThat(alVueVueViConditionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alVueVueViConditionCriteriaCopyCreatesNullFilterTest() {
        var alVueVueViConditionCriteria = new AlVueVueViConditionCriteria();
        var copy = alVueVueViConditionCriteria.copy();

        assertThat(alVueVueViConditionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueViConditionCriteria)
        );
    }

    @Test
    void alVueVueViConditionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alVueVueViConditionCriteria = new AlVueVueViConditionCriteria();
        setAllFilters(alVueVueViConditionCriteria);

        var copy = alVueVueViConditionCriteria.copy();

        assertThat(alVueVueViConditionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alVueVueViConditionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alVueVueViConditionCriteria = new AlVueVueViConditionCriteria();

        assertThat(alVueVueViConditionCriteria).hasToString("AlVueVueViConditionCriteria{}");
    }

    private static void setAllFilters(AlVueVueViConditionCriteria alVueVueViConditionCriteria) {
        alVueVueViConditionCriteria.id();
        alVueVueViConditionCriteria.subjectType();
        alVueVueViConditionCriteria.subject();
        alVueVueViConditionCriteria.action();
        alVueVueViConditionCriteria.note();
        alVueVueViConditionCriteria.parentId();
        alVueVueViConditionCriteria.applicationId();
        alVueVueViConditionCriteria.distinct();
    }

    private static Condition<AlVueVueViConditionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlVueVueViConditionCriteria> copyFiltersAre(
        AlVueVueViConditionCriteria copy,
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
