package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPedroTaxViCriteriaTest {

    @Test
    void newAlPedroTaxViCriteriaHasAllFiltersNullTest() {
        var alPedroTaxViCriteria = new AlPedroTaxViCriteria();
        assertThat(alPedroTaxViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPedroTaxViCriteriaFluentMethodsCreatesFiltersTest() {
        var alPedroTaxViCriteria = new AlPedroTaxViCriteria();

        setAllFilters(alPedroTaxViCriteria);

        assertThat(alPedroTaxViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPedroTaxViCriteriaCopyCreatesNullFilterTest() {
        var alPedroTaxViCriteria = new AlPedroTaxViCriteria();
        var copy = alPedroTaxViCriteria.copy();

        assertThat(alPedroTaxViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPedroTaxViCriteria)
        );
    }

    @Test
    void alPedroTaxViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPedroTaxViCriteria = new AlPedroTaxViCriteria();
        setAllFilters(alPedroTaxViCriteria);

        var copy = alPedroTaxViCriteria.copy();

        assertThat(alPedroTaxViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPedroTaxViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPedroTaxViCriteria = new AlPedroTaxViCriteria();

        assertThat(alPedroTaxViCriteria).hasToString("AlPedroTaxViCriteria{}");
    }

    private static void setAllFilters(AlPedroTaxViCriteria alPedroTaxViCriteria) {
        alPedroTaxViCriteria.id();
        alPedroTaxViCriteria.name();
        alPedroTaxViCriteria.description();
        alPedroTaxViCriteria.weight();
        alPedroTaxViCriteria.propertyType();
        alPedroTaxViCriteria.applicationId();
        alPedroTaxViCriteria.attributeTermsId();
        alPedroTaxViCriteria.distinct();
    }

    private static Condition<AlPedroTaxViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getPropertyType()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getAttributeTermsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlPedroTaxViCriteria> copyFiltersAre(
        AlPedroTaxViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getPropertyType(), copy.getPropertyType()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getAttributeTermsId(), copy.getAttributeTermsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
