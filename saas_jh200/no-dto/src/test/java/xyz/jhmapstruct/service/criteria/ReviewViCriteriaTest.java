package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewViCriteriaTest {

    @Test
    void newReviewViCriteriaHasAllFiltersNullTest() {
        var reviewViCriteria = new ReviewViCriteria();
        assertThat(reviewViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewViCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewViCriteria = new ReviewViCriteria();

        setAllFilters(reviewViCriteria);

        assertThat(reviewViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewViCriteriaCopyCreatesNullFilterTest() {
        var reviewViCriteria = new ReviewViCriteria();
        var copy = reviewViCriteria.copy();

        assertThat(reviewViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewViCriteria)
        );
    }

    @Test
    void reviewViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewViCriteria = new ReviewViCriteria();
        setAllFilters(reviewViCriteria);

        var copy = reviewViCriteria.copy();

        assertThat(reviewViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewViCriteria = new ReviewViCriteria();

        assertThat(reviewViCriteria).hasToString("ReviewViCriteria{}");
    }

    private static void setAllFilters(ReviewViCriteria reviewViCriteria) {
        reviewViCriteria.id();
        reviewViCriteria.rating();
        reviewViCriteria.reviewDate();
        reviewViCriteria.productId();
        reviewViCriteria.tenantId();
        reviewViCriteria.distinct();
    }

    private static Condition<ReviewViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewViCriteria> copyFiltersAre(ReviewViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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