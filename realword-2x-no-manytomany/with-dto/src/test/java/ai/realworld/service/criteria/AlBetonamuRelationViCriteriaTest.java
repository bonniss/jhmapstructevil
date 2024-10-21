package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationViCriteriaTest {

    @Test
    void newAlBetonamuRelationViCriteriaHasAllFiltersNullTest() {
        var alBetonamuRelationViCriteria = new AlBetonamuRelationViCriteria();
        assertThat(alBetonamuRelationViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alBetonamuRelationViCriteriaFluentMethodsCreatesFiltersTest() {
        var alBetonamuRelationViCriteria = new AlBetonamuRelationViCriteria();

        setAllFilters(alBetonamuRelationViCriteria);

        assertThat(alBetonamuRelationViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alBetonamuRelationViCriteriaCopyCreatesNullFilterTest() {
        var alBetonamuRelationViCriteria = new AlBetonamuRelationViCriteria();
        var copy = alBetonamuRelationViCriteria.copy();

        assertThat(alBetonamuRelationViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alBetonamuRelationViCriteria)
        );
    }

    @Test
    void alBetonamuRelationViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alBetonamuRelationViCriteria = new AlBetonamuRelationViCriteria();
        setAllFilters(alBetonamuRelationViCriteria);

        var copy = alBetonamuRelationViCriteria.copy();

        assertThat(alBetonamuRelationViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alBetonamuRelationViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alBetonamuRelationViCriteria = new AlBetonamuRelationViCriteria();

        assertThat(alBetonamuRelationViCriteria).hasToString("AlBetonamuRelationViCriteria{}");
    }

    private static void setAllFilters(AlBetonamuRelationViCriteria alBetonamuRelationViCriteria) {
        alBetonamuRelationViCriteria.id();
        alBetonamuRelationViCriteria.type();
        alBetonamuRelationViCriteria.supplierId();
        alBetonamuRelationViCriteria.customerId();
        alBetonamuRelationViCriteria.applicationId();
        alBetonamuRelationViCriteria.discountsId();
        alBetonamuRelationViCriteria.distinct();
    }

    private static Condition<AlBetonamuRelationViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlBetonamuRelationViCriteria> copyFiltersAre(
        AlBetonamuRelationViCriteria copy,
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
