package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeCriteriaTest {

    @Test
    void newAlLeandroPlayingTimeCriteriaHasAllFiltersNullTest() {
        var alLeandroPlayingTimeCriteria = new AlLeandroPlayingTimeCriteria();
        assertThat(alLeandroPlayingTimeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLeandroPlayingTimeCriteriaFluentMethodsCreatesFiltersTest() {
        var alLeandroPlayingTimeCriteria = new AlLeandroPlayingTimeCriteria();

        setAllFilters(alLeandroPlayingTimeCriteria);

        assertThat(alLeandroPlayingTimeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLeandroPlayingTimeCriteriaCopyCreatesNullFilterTest() {
        var alLeandroPlayingTimeCriteria = new AlLeandroPlayingTimeCriteria();
        var copy = alLeandroPlayingTimeCriteria.copy();

        assertThat(alLeandroPlayingTimeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLeandroPlayingTimeCriteria)
        );
    }

    @Test
    void alLeandroPlayingTimeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLeandroPlayingTimeCriteria = new AlLeandroPlayingTimeCriteria();
        setAllFilters(alLeandroPlayingTimeCriteria);

        var copy = alLeandroPlayingTimeCriteria.copy();

        assertThat(alLeandroPlayingTimeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLeandroPlayingTimeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLeandroPlayingTimeCriteria = new AlLeandroPlayingTimeCriteria();

        assertThat(alLeandroPlayingTimeCriteria).hasToString("AlLeandroPlayingTimeCriteria{}");
    }

    private static void setAllFilters(AlLeandroPlayingTimeCriteria alLeandroPlayingTimeCriteria) {
        alLeandroPlayingTimeCriteria.id();
        alLeandroPlayingTimeCriteria.status();
        alLeandroPlayingTimeCriteria.wonDate();
        alLeandroPlayingTimeCriteria.sentAwardToPlayerAt();
        alLeandroPlayingTimeCriteria.sentAwardToPlayerBy();
        alLeandroPlayingTimeCriteria.playerReceivedTheAwardAt();
        alLeandroPlayingTimeCriteria.playSourceTime();
        alLeandroPlayingTimeCriteria.miniGameId();
        alLeandroPlayingTimeCriteria.userId();
        alLeandroPlayingTimeCriteria.awardId();
        alLeandroPlayingTimeCriteria.applicationId();
        alLeandroPlayingTimeCriteria.distinct();
    }

    private static Condition<AlLeandroPlayingTimeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getWonDate()) &&
                condition.apply(criteria.getSentAwardToPlayerAt()) &&
                condition.apply(criteria.getSentAwardToPlayerBy()) &&
                condition.apply(criteria.getPlayerReceivedTheAwardAt()) &&
                condition.apply(criteria.getPlaySourceTime()) &&
                condition.apply(criteria.getMiniGameId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getAwardId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlLeandroPlayingTimeCriteria> copyFiltersAre(
        AlLeandroPlayingTimeCriteria copy,
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
                condition.apply(criteria.getMiniGameId(), copy.getMiniGameId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getAwardId(), copy.getAwardId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
