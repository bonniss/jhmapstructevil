package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPounderCriteriaTest {

    @Test
    void newAlPounderCriteriaHasAllFiltersNullTest() {
        var alPounderCriteria = new AlPounderCriteria();
        assertThat(alPounderCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPounderCriteriaFluentMethodsCreatesFiltersTest() {
        var alPounderCriteria = new AlPounderCriteria();

        setAllFilters(alPounderCriteria);

        assertThat(alPounderCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPounderCriteriaCopyCreatesNullFilterTest() {
        var alPounderCriteria = new AlPounderCriteria();
        var copy = alPounderCriteria.copy();

        assertThat(alPounderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPounderCriteria)
        );
    }

    @Test
    void alPounderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPounderCriteria = new AlPounderCriteria();
        setAllFilters(alPounderCriteria);

        var copy = alPounderCriteria.copy();

        assertThat(alPounderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPounderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPounderCriteria = new AlPounderCriteria();

        assertThat(alPounderCriteria).hasToString("AlPounderCriteria{}");
    }

    private static void setAllFilters(AlPounderCriteria alPounderCriteria) {
        alPounderCriteria.id();
        alPounderCriteria.name();
        alPounderCriteria.weight();
        alPounderCriteria.attributeTaxonomyId();
        alPounderCriteria.applicationId();
        alPounderCriteria.distinct();
    }

    private static Condition<AlPounderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPounderCriteria> copyFiltersAre(AlPounderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
