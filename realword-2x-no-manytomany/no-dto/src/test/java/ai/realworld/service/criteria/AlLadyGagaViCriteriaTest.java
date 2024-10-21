package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLadyGagaViCriteriaTest {

    @Test
    void newAlLadyGagaViCriteriaHasAllFiltersNullTest() {
        var alLadyGagaViCriteria = new AlLadyGagaViCriteria();
        assertThat(alLadyGagaViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLadyGagaViCriteriaFluentMethodsCreatesFiltersTest() {
        var alLadyGagaViCriteria = new AlLadyGagaViCriteria();

        setAllFilters(alLadyGagaViCriteria);

        assertThat(alLadyGagaViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLadyGagaViCriteriaCopyCreatesNullFilterTest() {
        var alLadyGagaViCriteria = new AlLadyGagaViCriteria();
        var copy = alLadyGagaViCriteria.copy();

        assertThat(alLadyGagaViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLadyGagaViCriteria)
        );
    }

    @Test
    void alLadyGagaViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLadyGagaViCriteria = new AlLadyGagaViCriteria();
        setAllFilters(alLadyGagaViCriteria);

        var copy = alLadyGagaViCriteria.copy();

        assertThat(alLadyGagaViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLadyGagaViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLadyGagaViCriteria = new AlLadyGagaViCriteria();

        assertThat(alLadyGagaViCriteria).hasToString("AlLadyGagaViCriteria{}");
    }

    private static void setAllFilters(AlLadyGagaViCriteria alLadyGagaViCriteria) {
        alLadyGagaViCriteria.id();
        alLadyGagaViCriteria.name();
        alLadyGagaViCriteria.descriptionHeitiga();
        alLadyGagaViCriteria.addressId();
        alLadyGagaViCriteria.avatarId();
        alLadyGagaViCriteria.applicationId();
        alLadyGagaViCriteria.distinct();
    }

    private static Condition<AlLadyGagaViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescriptionHeitiga()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlLadyGagaViCriteria> copyFiltersAre(
        AlLadyGagaViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescriptionHeitiga(), copy.getDescriptionHeitiga()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
