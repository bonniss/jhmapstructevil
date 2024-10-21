package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlMenityCriteriaTest {

    @Test
    void newAlMenityCriteriaHasAllFiltersNullTest() {
        var alMenityCriteria = new AlMenityCriteria();
        assertThat(alMenityCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alMenityCriteriaFluentMethodsCreatesFiltersTest() {
        var alMenityCriteria = new AlMenityCriteria();

        setAllFilters(alMenityCriteria);

        assertThat(alMenityCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alMenityCriteriaCopyCreatesNullFilterTest() {
        var alMenityCriteria = new AlMenityCriteria();
        var copy = alMenityCriteria.copy();

        assertThat(alMenityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alMenityCriteria)
        );
    }

    @Test
    void alMenityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alMenityCriteria = new AlMenityCriteria();
        setAllFilters(alMenityCriteria);

        var copy = alMenityCriteria.copy();

        assertThat(alMenityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alMenityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alMenityCriteria = new AlMenityCriteria();

        assertThat(alMenityCriteria).hasToString("AlMenityCriteria{}");
    }

    private static void setAllFilters(AlMenityCriteria alMenityCriteria) {
        alMenityCriteria.id();
        alMenityCriteria.name();
        alMenityCriteria.iconSvg();
        alMenityCriteria.propertyType();
        alMenityCriteria.applicationId();
        alMenityCriteria.distinct();
    }

    private static Condition<AlMenityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIconSvg()) &&
                condition.apply(criteria.getPropertyType()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlMenityCriteria> copyFiltersAre(AlMenityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIconSvg(), copy.getIconSvg()) &&
                condition.apply(criteria.getPropertyType(), copy.getPropertyType()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
