package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlBestToothCriteriaTest {

    @Test
    void newAlBestToothCriteriaHasAllFiltersNullTest() {
        var alBestToothCriteria = new AlBestToothCriteria();
        assertThat(alBestToothCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alBestToothCriteriaFluentMethodsCreatesFiltersTest() {
        var alBestToothCriteria = new AlBestToothCriteria();

        setAllFilters(alBestToothCriteria);

        assertThat(alBestToothCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alBestToothCriteriaCopyCreatesNullFilterTest() {
        var alBestToothCriteria = new AlBestToothCriteria();
        var copy = alBestToothCriteria.copy();

        assertThat(alBestToothCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alBestToothCriteria)
        );
    }

    @Test
    void alBestToothCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alBestToothCriteria = new AlBestToothCriteria();
        setAllFilters(alBestToothCriteria);

        var copy = alBestToothCriteria.copy();

        assertThat(alBestToothCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alBestToothCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alBestToothCriteria = new AlBestToothCriteria();

        assertThat(alBestToothCriteria).hasToString("AlBestToothCriteria{}");
    }

    private static void setAllFilters(AlBestToothCriteria alBestToothCriteria) {
        alBestToothCriteria.id();
        alBestToothCriteria.name();
        alBestToothCriteria.applicationId();
        alBestToothCriteria.articleId();
        alBestToothCriteria.distinct();
    }

    private static Condition<AlBestToothCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getArticleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlBestToothCriteria> copyFiltersAre(AlBestToothCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getArticleId(), copy.getArticleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
