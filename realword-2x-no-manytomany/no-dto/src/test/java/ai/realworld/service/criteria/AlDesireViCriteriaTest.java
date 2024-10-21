package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlDesireViCriteriaTest {

    @Test
    void newAlDesireViCriteriaHasAllFiltersNullTest() {
        var alDesireViCriteria = new AlDesireViCriteria();
        assertThat(alDesireViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alDesireViCriteriaFluentMethodsCreatesFiltersTest() {
        var alDesireViCriteria = new AlDesireViCriteria();

        setAllFilters(alDesireViCriteria);

        assertThat(alDesireViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alDesireViCriteriaCopyCreatesNullFilterTest() {
        var alDesireViCriteria = new AlDesireViCriteria();
        var copy = alDesireViCriteria.copy();

        assertThat(alDesireViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alDesireViCriteria)
        );
    }

    @Test
    void alDesireViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alDesireViCriteria = new AlDesireViCriteria();
        setAllFilters(alDesireViCriteria);

        var copy = alDesireViCriteria.copy();

        assertThat(alDesireViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alDesireViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alDesireViCriteria = new AlDesireViCriteria();

        assertThat(alDesireViCriteria).hasToString("AlDesireViCriteria{}");
    }

    private static void setAllFilters(AlDesireViCriteria alDesireViCriteria) {
        alDesireViCriteria.id();
        alDesireViCriteria.name();
        alDesireViCriteria.weight();
        alDesireViCriteria.probabilityOfWinning();
        alDesireViCriteria.maximumWinningTime();
        alDesireViCriteria.isWinningTimeLimited();
        alDesireViCriteria.awardResultType();
        alDesireViCriteria.awardReference();
        alDesireViCriteria.isDefault();
        alDesireViCriteria.imageId();
        alDesireViCriteria.maggiId();
        alDesireViCriteria.distinct();
    }

    private static Condition<AlDesireViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getMaggiId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlDesireViCriteria> copyFiltersAre(AlDesireViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
                condition.apply(criteria.getMaggiId(), copy.getMaggiId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
