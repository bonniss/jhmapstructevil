package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPowerShellCriteriaTest {

    @Test
    void newAlPowerShellCriteriaHasAllFiltersNullTest() {
        var alPowerShellCriteria = new AlPowerShellCriteria();
        assertThat(alPowerShellCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPowerShellCriteriaFluentMethodsCreatesFiltersTest() {
        var alPowerShellCriteria = new AlPowerShellCriteria();

        setAllFilters(alPowerShellCriteria);

        assertThat(alPowerShellCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPowerShellCriteriaCopyCreatesNullFilterTest() {
        var alPowerShellCriteria = new AlPowerShellCriteria();
        var copy = alPowerShellCriteria.copy();

        assertThat(alPowerShellCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPowerShellCriteria)
        );
    }

    @Test
    void alPowerShellCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPowerShellCriteria = new AlPowerShellCriteria();
        setAllFilters(alPowerShellCriteria);

        var copy = alPowerShellCriteria.copy();

        assertThat(alPowerShellCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPowerShellCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPowerShellCriteria = new AlPowerShellCriteria();

        assertThat(alPowerShellCriteria).hasToString("AlPowerShellCriteria{}");
    }

    private static void setAllFilters(AlPowerShellCriteria alPowerShellCriteria) {
        alPowerShellCriteria.id();
        alPowerShellCriteria.value();
        alPowerShellCriteria.propertyProfileId();
        alPowerShellCriteria.attributeTermId();
        alPowerShellCriteria.applicationId();
        alPowerShellCriteria.distinct();
    }

    private static Condition<AlPowerShellCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPowerShellCriteria> copyFiltersAre(
        AlPowerShellCriteria copy,
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
