package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlPedroTaxCriteriaTest {

    @Test
    void newAlPedroTaxCriteriaHasAllFiltersNullTest() {
        var alPedroTaxCriteria = new AlPedroTaxCriteria();
        assertThat(alPedroTaxCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alPedroTaxCriteriaFluentMethodsCreatesFiltersTest() {
        var alPedroTaxCriteria = new AlPedroTaxCriteria();

        setAllFilters(alPedroTaxCriteria);

        assertThat(alPedroTaxCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alPedroTaxCriteriaCopyCreatesNullFilterTest() {
        var alPedroTaxCriteria = new AlPedroTaxCriteria();
        var copy = alPedroTaxCriteria.copy();

        assertThat(alPedroTaxCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alPedroTaxCriteria)
        );
    }

    @Test
    void alPedroTaxCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alPedroTaxCriteria = new AlPedroTaxCriteria();
        setAllFilters(alPedroTaxCriteria);

        var copy = alPedroTaxCriteria.copy();

        assertThat(alPedroTaxCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alPedroTaxCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alPedroTaxCriteria = new AlPedroTaxCriteria();

        assertThat(alPedroTaxCriteria).hasToString("AlPedroTaxCriteria{}");
    }

    private static void setAllFilters(AlPedroTaxCriteria alPedroTaxCriteria) {
        alPedroTaxCriteria.id();
        alPedroTaxCriteria.name();
        alPedroTaxCriteria.description();
        alPedroTaxCriteria.weight();
        alPedroTaxCriteria.propertyType();
        alPedroTaxCriteria.applicationId();
        alPedroTaxCriteria.attributeTermsId();
        alPedroTaxCriteria.distinct();
    }

    private static Condition<AlPedroTaxCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlPedroTaxCriteria> copyFiltersAre(AlPedroTaxCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
