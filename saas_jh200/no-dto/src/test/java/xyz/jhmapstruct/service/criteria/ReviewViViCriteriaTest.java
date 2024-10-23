package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewViViCriteriaTest {

    @Test
    void newReviewViViCriteriaHasAllFiltersNullTest() {
        var reviewViViCriteria = new ReviewViViCriteria();
        assertThat(reviewViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewViViCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewViViCriteria = new ReviewViViCriteria();

        setAllFilters(reviewViViCriteria);

        assertThat(reviewViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewViViCriteriaCopyCreatesNullFilterTest() {
        var reviewViViCriteria = new ReviewViViCriteria();
        var copy = reviewViViCriteria.copy();

        assertThat(reviewViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewViViCriteria)
        );
    }

    @Test
    void reviewViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewViViCriteria = new ReviewViViCriteria();
        setAllFilters(reviewViViCriteria);

        var copy = reviewViViCriteria.copy();

        assertThat(reviewViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewViViCriteria = new ReviewViViCriteria();

        assertThat(reviewViViCriteria).hasToString("ReviewViViCriteria{}");
    }

    private static void setAllFilters(ReviewViViCriteria reviewViViCriteria) {
        reviewViViCriteria.id();
        reviewViViCriteria.rating();
        reviewViViCriteria.reviewDate();
        reviewViViCriteria.productId();
        reviewViViCriteria.tenantId();
        reviewViViCriteria.distinct();
    }

    private static Condition<ReviewViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRating()) &&
                condition.apply(criteria.getReviewDate()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReviewViViCriteria> copyFiltersAre(ReviewViViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRating(), copy.getRating()) &&
                condition.apply(criteria.getReviewDate(), copy.getReviewDate()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
