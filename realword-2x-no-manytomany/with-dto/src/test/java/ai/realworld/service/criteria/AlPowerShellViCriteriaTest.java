package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPowerShellViCriteriaTest {

    @Test
    void newAlPowerShellViCriteriaHasAllFiltersNullTest() {
        var alPowerShellViCriteria = new AlPowerShellViCriteria();
        assertThat(alPowerShellViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPowerShellViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPowerShellViCriteria = new AlPowerShellViCriteria();

        setAllFilters(alPowerShellViCriteria);

        assertThat(alPowerShellViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPowerShellViCriteriaCopyCreatesNullFilterTest() {
        var alPowerShellViCriteria = new AlPowerShellViCriteria();
        var copy = alPowerShellViCriteria.copy();

        assertThat(alPowerShellViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPowerShellViCriteria)
        );
    }

    @Test
    void alPowerShellViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPowerShellViCriteria = new AlPowerShellViCriteria();
        setAllFilters(alPowerShellViCriteria);

        var copy = alPowerShellViCriteria.copy();

        assertThat(alPowerShellViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPowerShellViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPowerShellViCriteria = new AlPowerShellViCriteria();

        assertThat(alPowerShellViCriteria).hasToString("AlPowerShellViCriteria{}");
    }

    private static void setAllFilters(AlPowerShellViCriteria alPowerShellViCriteria) {
        alPowerShellViCriteria.id();
        alPowerShellViCriteria.value();
        alPowerShellViCriteria.propertyProfileId();
        alPowerShellViCriteria.attributeTermId();
        alPowerShellViCriteria.applicationId();
        alPowerShellViCriteria.distinct();
    }

    private static Condition<AlPowerShellViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getPropertyProfileId()) &&
                condition.apply(criteria.getAttributeTermId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPowerShellViCriteria> copyFiltersAre(
        AlPowerShellViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getPropertyProfileId(), copy.getPropertyProfileId()) &&
                condition.apply(criteria.getAttributeTermId(), copy.getAttributeTermId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
