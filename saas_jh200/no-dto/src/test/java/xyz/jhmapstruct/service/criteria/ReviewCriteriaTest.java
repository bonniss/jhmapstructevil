package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewCriteriaTest {

    @Test
    void newReviewCriteriaHasAllFiltersNullTest() {
        var reviewCriteria = new ReviewCriteria();
        assertThat(reviewCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewCriteria = new ReviewCriteria();

        setAllFilters(reviewCriteria);

        assertThat(reviewCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewCriteriaCopyCreatesNullFilterTest() {
        var reviewCriteria = new ReviewCriteria();
        var copy = reviewCriteria.copy();

        assertThat(reviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewCriteria)
        );
    }

    @Test
    void reviewCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewCriteria = new ReviewCriteria();
        setAllFilters(reviewCriteria);

        var copy = reviewCriteria.copy();

        assertThat(reviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewCriteria = new ReviewCriteria();

        assertThat(reviewCriteria).hasToString("ReviewCriteria{}");
    }

    private static void setAllFilters(ReviewCriteria reviewCriteria) {
        reviewCriteria.id();
        reviewCriteria.rating();
        reviewCriteria.reviewDate();
        reviewCriteria.productId();
        reviewCriteria.tenantId();
        reviewCriteria.distinct();
    }

    private static Condition<ReviewCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewCriteria> copyFiltersAre(ReviewCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
