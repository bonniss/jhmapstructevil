package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLeandroCriteriaTest {

    @Test
    void newAlLeandroCriteriaHasAllFiltersNullTest() {
        var alLeandroCriteria = new AlLeandroCriteria();
        assertThat(alLeandroCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLeandroCriteriaFluentMethodsCreatesFiltersTest() {
        var alLeandroCriteria = new AlLeandroCriteria();

        setAllFilters(alLeandroCriteria);

        assertThat(alLeandroCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLeandroCriteriaCopyCreatesNullFilterTest() {
        var alLeandroCriteria = new AlLeandroCriteria();
        var copy = alLeandroCriteria.copy();

        assertThat(alLeandroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLeandroCriteria)
        );
    }

    @Test
    void alLeandroCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLeandroCriteria = new AlLeandroCriteria();
        setAllFilters(alLeandroCriteria);

        var copy = alLeandroCriteria.copy();

        assertThat(alLeandroCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLeandroCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLeandroCriteria = new AlLeandroCriteria();

        assertThat(alLeandroCriteria).hasToString("AlLeandroCriteria{}");
    }

    private static void setAllFilters(AlLeandroCriteria alLeandroCriteria) {
        alLeandroCriteria.id();
        alLeandroCriteria.name();
        alLeandroCriteria.weight();
        alLeandroCriteria.description();
        alLeandroCriteria.fromDate();
        alLeandroCriteria.toDate();
        alLeandroCriteria.isEnabled();
        alLeandroCriteria.separateWinningByPeriods();
        alLeandroCriteria.programBackgroundId();
        alLeandroCriteria.wheelBackgroundId();
        alLeandroCriteria.applicationId();
        alLeandroCriteria.awardsId();
        alLeandroCriteria.distinct();
    }

    private static Condition<AlLeandroCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getToDate()) &&
                condition.apply(criteria.getIsEnabled()) &&
                condition.apply(criteria.getSeparateWinningByPeriods()) &&
                condition.apply(criteria.getProgramBackgroundId()) &&
                condition.apply(criteria.getWheelBackgroundId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getAwardsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlLeandroCriteria> copyFiltersAre(AlLeandroCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getToDate(), copy.getToDate()) &&
                condition.apply(criteria.getIsEnabled(), copy.getIsEnabled()) &&
                condition.apply(criteria.getSeparateWinningByPeriods(), copy.getSeparateWinningByPeriods()) &&
                condition.apply(criteria.getProgramBackgroundId(), copy.getProgramBackgroundId()) &&
                condition.apply(criteria.getWheelBackgroundId(), copy.getWheelBackgroundId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getAwardsId(), copy.getAwardsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
