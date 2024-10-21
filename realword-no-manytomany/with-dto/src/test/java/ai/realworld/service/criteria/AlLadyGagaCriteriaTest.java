package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLadyGagaCriteriaTest {

    @Test
    void newAlLadyGagaCriteriaHasAllFiltersNullTest() {
        var alLadyGagaCriteria = new AlLadyGagaCriteria();
        assertThat(alLadyGagaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLadyGagaCriteriaFluentMethodsCreatesFiltersTest() {
        var alLadyGagaCriteria = new AlLadyGagaCriteria();

        setAllFilters(alLadyGagaCriteria);

        assertThat(alLadyGagaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLadyGagaCriteriaCopyCreatesNullFilterTest() {
        var alLadyGagaCriteria = new AlLadyGagaCriteria();
        var copy = alLadyGagaCriteria.copy();

        assertThat(alLadyGagaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLadyGagaCriteria)
        );
    }

    @Test
    void alLadyGagaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLadyGagaCriteria = new AlLadyGagaCriteria();
        setAllFilters(alLadyGagaCriteria);

        var copy = alLadyGagaCriteria.copy();

        assertThat(alLadyGagaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLadyGagaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLadyGagaCriteria = new AlLadyGagaCriteria();

        assertThat(alLadyGagaCriteria).hasToString("AlLadyGagaCriteria{}");
    }

    private static void setAllFilters(AlLadyGagaCriteria alLadyGagaCriteria) {
        alLadyGagaCriteria.id();
        alLadyGagaCriteria.name();
        alLadyGagaCriteria.descriptionHeitiga();
        alLadyGagaCriteria.addressId();
        alLadyGagaCriteria.avatarId();
        alLadyGagaCriteria.applicationId();
        alLadyGagaCriteria.distinct();
    }

    private static Condition<AlLadyGagaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<AlLadyGagaCriteria> copyFiltersAre(AlLadyGagaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
