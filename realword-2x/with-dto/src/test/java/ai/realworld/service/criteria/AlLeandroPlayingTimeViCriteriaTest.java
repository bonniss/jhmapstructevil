package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeViCriteriaTest {

    @Test
    void newAlLeandroPlayingTimeViCriteriaHasAllFiltersNullTest() {
        var alLeandroPlayingTimeViCriteria = new AlLeandroPlayingTimeViCriteria();
        assertThat(alLeandroPlayingTimeViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLeandroPlayingTimeViCriteriaFluentMethodsCreatesFiltersTest() {
        var alLeandroPlayingTimeViCriteria = new AlLeandroPlayingTimeViCriteria();

        setAllFilters(alLeandroPlayingTimeViCriteria);

        assertThat(alLeandroPlayingTimeViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLeandroPlayingTimeViCriteriaCopyCreatesNullFilterTest() {
        var alLeandroPlayingTimeViCriteria = new AlLeandroPlayingTimeViCriteria();
        var copy = alLeandroPlayingTimeViCriteria.copy();

        assertThat(alLeandroPlayingTimeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLeandroPlayingTimeViCriteria)
        );
    }

    @Test
    void alLeandroPlayingTimeViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLeandroPlayingTimeViCriteria = new AlLeandroPlayingTimeViCriteria();
        setAllFilters(alLeandroPlayingTimeViCriteria);

        var copy = alLeandroPlayingTimeViCriteria.copy();

        assertThat(alLeandroPlayingTimeViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLeandroPlayingTimeViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLeandroPlayingTimeViCriteria = new AlLeandroPlayingTimeViCriteria();

        assertThat(alLeandroPlayingTimeViCriteria).hasToString("AlLeandroPlayingTimeViCriteria{}");
    }

    private static void setAllFilters(AlLeandroPlayingTimeViCriteria alLeandroPlayingTimeViCriteria) {
        alLeandroPlayingTimeViCriteria.id();
        alLeandroPlayingTimeViCriteria.status();
        alLeandroPlayingTimeViCriteria.wonDate();
        alLeandroPlayingTimeViCriteria.sentAwardToPlayerAt();
        alLeandroPlayingTimeViCriteria.sentAwardToPlayerBy();
        alLeandroPlayingTimeViCriteria.playerReceivedTheAwardAt();
        alLeandroPlayingTimeViCriteria.playSourceTime();
        alLeandroPlayingTimeViCriteria.maggiId();
        alLeandroPlayingTimeViCriteria.userId();
        alLeandroPlayingTimeViCriteria.awardId();
        alLeandroPlayingTimeViCriteria.applicationId();
        alLeandroPlayingTimeViCriteria.distinct();
    }

    private static Condition<AlLeandroPlayingTimeViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getWonDate()) &&
                condition.apply(criteria.getSentAwardToPlayerAt()) &&
                condition.apply(criteria.getSentAwardToPlayerBy()) &&
                condition.apply(criteria.getPlayerReceivedTheAwardAt()) &&
                condition.apply(criteria.getPlaySourceTime()) &&
                condition.apply(criteria.getMaggiId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getAwardId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlLeandroPlayingTimeViCriteria> copyFiltersAre(
        AlLeandroPlayingTimeViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getWonDate(), copy.getWonDate()) &&
                condition.apply(criteria.getSentAwardToPlayerAt(), copy.getSentAwardToPlayerAt()) &&
                condition.apply(criteria.getSentAwardToPlayerBy(), copy.getSentAwardToPlayerBy()) &&
                condition.apply(criteria.getPlayerReceivedTheAwardAt(), copy.getPlayerReceivedTheAwardAt()) &&
                condition.apply(criteria.getPlaySourceTime(), copy.getPlaySourceTime()) &&
                condition.apply(criteria.getMaggiId(), copy.getMaggiId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getAwardId(), copy.getAwardId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
