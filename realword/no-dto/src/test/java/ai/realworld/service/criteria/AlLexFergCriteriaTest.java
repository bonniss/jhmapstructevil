package ai.realworld.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlLexFergCriteriaTest {

    @Test
    void newAlLexFergCriteriaHasAllFiltersNullTest() {
        var alLexFergCriteria = new AlLexFergCriteria();
        assertThat(alLexFergCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void alLexFergCriteriaFluentMethodsCreatesFiltersTest() {
        var alLexFergCriteria = new AlLexFergCriteria();

        setAllFilters(alLexFergCriteria);

        assertThat(alLexFergCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void alLexFergCriteriaCopyCreatesNullFilterTest() {
        var alLexFergCriteria = new AlLexFergCriteria();
        var copy = alLexFergCriteria.copy();

        assertThat(alLexFergCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(alLexFergCriteria)
        );
    }

    @Test
    void alLexFergCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alLexFergCriteria = new AlLexFergCriteria();
        setAllFilters(alLexFergCriteria);

        var copy = alLexFergCriteria.copy();

        assertThat(alLexFergCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(alLexFergCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alLexFergCriteria = new AlLexFergCriteria();

        assertThat(alLexFergCriteria).hasToString("AlLexFergCriteria{}");
    }

    private static void setAllFilters(AlLexFergCriteria alLexFergCriteria) {
        alLexFergCriteria.id();
        alLexFergCriteria.title();
        alLexFergCriteria.slug();
        alLexFergCriteria.summary();
        alLexFergCriteria.contentHeitiga();
        alLexFergCriteria.publicationStatus();
        alLexFergCriteria.publishedDate();
        alLexFergCriteria.avatarId();
        alLexFergCriteria.categoryId();
        alLexFergCriteria.applicationId();
        alLexFergCriteria.tagId();
        alLexFergCriteria.distinct();
    }

    private static Condition<AlLexFergCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getSummary()) &&
                condition.apply(criteria.getContentHeitiga()) &&
                condition.apply(criteria.getPublicationStatus()) &&
                condition.apply(criteria.getPublishedDate()) &&
                condition.apply(criteria.getAvatarId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getApplicationId()) &&
                condition.apply(criteria.getTagId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlLexFergCriteria> copyFiltersAre(AlLexFergCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getSummary(), copy.getSummary()) &&
                condition.apply(criteria.getContentHeitiga(), copy.getContentHeitiga()) &&
                condition.apply(criteria.getPublicationStatus(), copy.getPublicationStatus()) &&
                condition.apply(criteria.getPublishedDate(), copy.getPublishedDate()) &&
                condition.apply(criteria.getAvatarId(), copy.getAvatarId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getApplicationId(), copy.getApplicationId()) &&
                condition.apply(criteria.getTagId(), copy.getTagId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
