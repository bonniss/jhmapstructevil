package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlDesireCriteriaTest {

    @Test
    void newAlDesireCriteriaHasAllFiltersNullTest() {
        var alDesireCriteria = new AlDesireCriteria();
        assertThat(alDesireCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alDesireCriteriaFluentMethodsCreatesFiltersTest() {
        var alDesireCriteria = new AlDesireCriteria();

        setAllFilters(alDesireCriteria);

        assertThat(alDesireCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alDesireCriteriaCopyCreatesNullFilterTest() {
        var alDesireCriteria = new AlDesireCriteria();
        var copy = alDesireCriteria.copy();

        assertThat(alDesireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alDesireCriteria)
        );
    }

    @Test
    void alDesireCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alDesireCriteria = new AlDesireCriteria();
        setAllFilters(alDesireCriteria);

        var copy = alDesireCriteria.copy();

        assertThat(alDesireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alDesireCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alDesireCriteria = new AlDesireCriteria();

        assertThat(alDesireCriteria).hasToString("AlDesireCriteria{}");
    }

    private static void setAllFilters(AlDesireCriteria alDesireCriteria) {
        alDesireCriteria.id();
        alDesireCriteria.name();
        alDesireCriteria.weight();
        alDesireCriteria.probabilityOfWinning();
        alDesireCriteria.maximumWinningTime();
        alDesireCriteria.isWinningTimeLimited();
        alDesireCriteria.awardResultType();
        alDesireCriteria.awardReference();
        alDesireCriteria.isDefault();
        alDesireCriteria.imageId();
        alDesireCriteria.miniGameId();
        alDesireCriteria.applicationId();
        alDesireCriteria.distinct();
    }

    private static Condition<AlDesireCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getProbabilityOfWinning()) &&
                condition.apply(criteria.getMaximumWinningTime()) &&
                condition.apply(criteria.getIsWinningTimeLimited()) &&
                condition.apply(criteria.getAwardResultType()) &&
                condition.apply(criteria.getAwardReference()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getImageId()) &&
                condition.apply(criteria.getMiniGameId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlDesireCriteria> copyFiltersAre(AlDesireCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getProbabilityOfWinning(), copy.getProbabilityOfWinning()) &&
                condition.apply(criteria.getMaximumWinningTime(), copy.getMaximumWinningTime()) &&
                condition.apply(criteria.getIsWinningTimeLimited(), copy.getIsWinningTimeLimited()) &&
                condition.apply(criteria.getAwardResultType(), copy.getAwardResultType()) &&
                condition.apply(criteria.getAwardReference(), copy.getAwardReference()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getImageId(), copy.getImageId()) &&
                condition.apply(criteria.getMiniGameId(), copy.getMiniGameId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
