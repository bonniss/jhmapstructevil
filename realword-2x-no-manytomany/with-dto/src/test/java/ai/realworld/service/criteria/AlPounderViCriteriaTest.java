package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPounderViCriteriaTest {

    @Test
    void newAlPounderViCriteriaHasAllFiltersNullTest() {
        var alPounderViCriteria = new AlPounderViCriteria();
        assertThat(alPounderViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPounderViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPounderViCriteria = new AlPounderViCriteria();

        setAllFilters(alPounderViCriteria);

        assertThat(alPounderViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPounderViCriteriaCopyCreatesNullFilterTest() {
        var alPounderViCriteria = new AlPounderViCriteria();
        var copy = alPounderViCriteria.copy();

        assertThat(alPounderViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPounderViCriteria)
        );
    }

    @Test
    void alPounderViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPounderViCriteria = new AlPounderViCriteria();
        setAllFilters(alPounderViCriteria);

        var copy = alPounderViCriteria.copy();

        assertThat(alPounderViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPounderViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPounderViCriteria = new AlPounderViCriteria();

        assertThat(alPounderViCriteria).hasToString("AlPounderViCriteria{}");
    }

    private static void setAllFilters(AlPounderViCriteria alPounderViCriteria) {
        alPounderViCriteria.id();
        alPounderViCriteria.name();
        alPounderViCriteria.weight();
        alPounderViCriteria.attributeTaxonomyId();
        alPounderViCriteria.applicationId();
        alPounderViCriteria.distinct();
    }

    private static Condition<AlPounderViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getAttributeTaxonomyId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPounderViCriteria> copyFiltersAre(AlPounderViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getAttributeTaxonomyId(), copy.getAttributeTaxonomyId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
