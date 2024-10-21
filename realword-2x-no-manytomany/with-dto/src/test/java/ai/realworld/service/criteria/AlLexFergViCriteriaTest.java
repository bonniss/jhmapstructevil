package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLexFergViCriteriaTest {

    @Test
    void newAlLexFergViCriteriaHasAllFiltersNullTest() {
        var alLexFergViCriteria = new AlLexFergViCriteria();
        assertThat(alLexFergViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLexFergViCriteriaFluentMethodsCreatesFiltersTest() {
        var alLexFergViCriteria = new AlLexFergViCriteria();

        setAllFilters(alLexFergViCriteria);

        assertThat(alLexFergViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLexFergViCriteriaCopyCreatesNullFilterTest() {
        var alLexFergViCriteria = new AlLexFergViCriteria();
        var copy = alLexFergViCriteria.copy();

        assertThat(alLexFergViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLexFergViCriteria)
        );
    }

    @Test
    void alLexFergViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLexFergViCriteria = new AlLexFergViCriteria();
        setAllFilters(alLexFergViCriteria);

        var copy = alLexFergViCriteria.copy();

        assertThat(alLexFergViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLexFergViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLexFergViCriteria = new AlLexFergViCriteria();

        assertThat(alLexFergViCriteria).hasToString("AlLexFergViCriteria{}");
    }

    private static void setAllFilters(AlLexFergViCriteria alLexFergViCriteria) {
        alLexFergViCriteria.id();
        alLexFergViCriteria.title();
        alLexFergViCriteria.slug();
        alLexFergViCriteria.summary();
        alLexFergViCriteria.contentHeitiga();
        alLexFergViCriteria.publicationStatus();
        alLexFergViCriteria.publishedDate();
        alLexFergViCriteria.distinct();
    }

    private static Condition<AlLexFergViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getSummary()) &&
                condition.apply(criteria.getContentHeitiga()) &&
                condition.apply(criteria.getPublicationStatus()) &&
                condition.apply(criteria.getPublishedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlLexFergViCriteria> copyFiltersAre(AlLexFergViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getSummary(), copy.getSummary()) &&
                condition.apply(criteria.getContentHeitiga(), copy.getContentHeitiga()) &&
                condition.apply(criteria.getPublicationStatus(), copy.getPublicationStatus()) &&
                condition.apply(criteria.getPublishedDate(), copy.getPublishedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
