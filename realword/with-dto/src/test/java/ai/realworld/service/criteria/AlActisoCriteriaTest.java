package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlActisoCriteriaTest {

    @Test
    void newAlActisoCriteriaHasAllFiltersNullTest() {
        var alActisoCriteria = new AlActisoCriteria();
        assertThat(alActisoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alActisoCriteriaFluentMethodsCreatesFiltersTest() {
        var alActisoCriteria = new AlActisoCriteria();

        setAllFilters(alActisoCriteria);

        assertThat(alActisoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alActisoCriteriaCopyCreatesNullFilterTest() {
        var alActisoCriteria = new AlActisoCriteria();
        var copy = alActisoCriteria.copy();

        assertThat(alActisoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alActisoCriteria)
        );
    }

    @Test
    void alActisoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alActisoCriteria = new AlActisoCriteria();
        setAllFilters(alActisoCriteria);

        var copy = alActisoCriteria.copy();

        assertThat(alActisoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alActisoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alActisoCriteria = new AlActisoCriteria();

        assertThat(alActisoCriteria).hasToString("AlActisoCriteria{}");
    }

    private static void setAllFilters(AlActisoCriteria alActisoCriteria) {
        alActisoCriteria.id();
        alActisoCriteria.key();
        alActisoCriteria.valueJason();
        alActisoCriteria.applicationId();
        alActisoCriteria.distinct();
    }

    private static Condition<AlActisoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getValueJason()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlActisoCriteria> copyFiltersAre(AlActisoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getValueJason(), copy.getValueJason()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
