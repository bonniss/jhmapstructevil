package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlBestToothViCriteriaTest {

    @Test
    void newAlBestToothViCriteriaHasAllFiltersNullTest() {
        var alBestToothViCriteria = new AlBestToothViCriteria();
        assertThat(alBestToothViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alBestToothViCriteriaFluentMethodsCreatesFiltersTest() {
        var alBestToothViCriteria = new AlBestToothViCriteria();

        setAllFilters(alBestToothViCriteria);

        assertThat(alBestToothViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alBestToothViCriteriaCopyCreatesNullFilterTest() {
        var alBestToothViCriteria = new AlBestToothViCriteria();
        var copy = alBestToothViCriteria.copy();

        assertThat(alBestToothViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alBestToothViCriteria)
        );
    }

    @Test
    void alBestToothViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alBestToothViCriteria = new AlBestToothViCriteria();
        setAllFilters(alBestToothViCriteria);

        var copy = alBestToothViCriteria.copy();

        assertThat(alBestToothViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alBestToothViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alBestToothViCriteria = new AlBestToothViCriteria();

        assertThat(alBestToothViCriteria).hasToString("AlBestToothViCriteria{}");
    }

    private static void setAllFilters(AlBestToothViCriteria alBestToothViCriteria) {
        alBestToothViCriteria.id();
        alBestToothViCriteria.name();
        alBestToothViCriteria.distinct();
    }

    private static Condition<AlBestToothViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlBestToothViCriteria> copyFiltersAre(
        AlBestToothViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
