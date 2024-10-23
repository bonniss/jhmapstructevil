package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewSigmaCriteriaTest {

    @Test
    void newReviewSigmaCriteriaHasAllFiltersNullTest() {
        var reviewSigmaCriteria = new ReviewSigmaCriteria();
        assertThat(reviewSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewSigmaCriteria = new ReviewSigmaCriteria();

        setAllFilters(reviewSigmaCriteria);

        assertThat(reviewSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewSigmaCriteriaCopyCreatesNullFilterTest() {
        var reviewSigmaCriteria = new ReviewSigmaCriteria();
        var copy = reviewSigmaCriteria.copy();

        assertThat(reviewSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewSigmaCriteria)
        );
    }

    @Test
    void reviewSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewSigmaCriteria = new ReviewSigmaCriteria();
        setAllFilters(reviewSigmaCriteria);

        var copy = reviewSigmaCriteria.copy();

        assertThat(reviewSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewSigmaCriteria = new ReviewSigmaCriteria();

        assertThat(reviewSigmaCriteria).hasToString("ReviewSigmaCriteria{}");
    }

    private static void setAllFilters(ReviewSigmaCriteria reviewSigmaCriteria) {
        reviewSigmaCriteria.id();
        reviewSigmaCriteria.rating();
        reviewSigmaCriteria.reviewDate();
        reviewSigmaCriteria.productId();
        reviewSigmaCriteria.tenantId();
        reviewSigmaCriteria.distinct();
    }

    private static Condition<ReviewSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewSigmaCriteria> copyFiltersAre(ReviewSigmaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
