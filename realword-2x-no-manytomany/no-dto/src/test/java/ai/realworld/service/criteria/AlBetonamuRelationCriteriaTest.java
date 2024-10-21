package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationCriteriaTest {

    @Test
    void newAlBetonamuRelationCriteriaHasAllFiltersNullTest() {
        var alBetonamuRelationCriteria = new AlBetonamuRelationCriteria();
        assertThat(alBetonamuRelationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alBetonamuRelationCriteriaFluentMethodsCreatesFiltersTest() {
        var alBetonamuRelationCriteria = new AlBetonamuRelationCriteria();

        setAllFilters(alBetonamuRelationCriteria);

        assertThat(alBetonamuRelationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alBetonamuRelationCriteriaCopyCreatesNullFilterTest() {
        var alBetonamuRelationCriteria = new AlBetonamuRelationCriteria();
        var copy = alBetonamuRelationCriteria.copy();

        assertThat(alBetonamuRelationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alBetonamuRelationCriteria)
        );
    }

    @Test
    void alBetonamuRelationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alBetonamuRelationCriteria = new AlBetonamuRelationCriteria();
        setAllFilters(alBetonamuRelationCriteria);

        var copy = alBetonamuRelationCriteria.copy();

        assertThat(alBetonamuRelationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alBetonamuRelationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alBetonamuRelationCriteria = new AlBetonamuRelationCriteria();

        assertThat(alBetonamuRelationCriteria).hasToString("AlBetonamuRelationCriteria{}");
    }

    private static void setAllFilters(AlBetonamuRelationCriteria alBetonamuRelationCriteria) {
        alBetonamuRelationCriteria.id();
        alBetonamuRelationCriteria.type();
        alBetonamuRelationCriteria.supplierId();
        alBetonamuRelationCriteria.customerId();
        alBetonamuRelationCriteria.applicationId();
        alBetonamuRelationCriteria.discountsId();
        alBetonamuRelationCriteria.distinct();
    }

    private static Condition<AlBetonamuRelationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getSupplierId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDiscountsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlBetonamuRelationCriteria> copyFiltersAre(
        AlBetonamuRelationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getSupplierId(), copy.getSupplierId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDiscountsId(), copy.getDiscountsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
