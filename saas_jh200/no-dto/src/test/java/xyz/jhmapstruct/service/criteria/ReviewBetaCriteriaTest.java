package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewBetaCriteriaTest {

    @Test
    void newReviewBetaCriteriaHasAllFiltersNullTest() {
        var reviewBetaCriteria = new ReviewBetaCriteria();
        assertThat(reviewBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewBetaCriteria = new ReviewBetaCriteria();

        setAllFilters(reviewBetaCriteria);

        assertThat(reviewBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewBetaCriteriaCopyCreatesNullFilterTest() {
        var reviewBetaCriteria = new ReviewBetaCriteria();
        var copy = reviewBetaCriteria.copy();

        assertThat(reviewBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewBetaCriteria)
        );
    }

    @Test
    void reviewBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewBetaCriteria = new ReviewBetaCriteria();
        setAllFilters(reviewBetaCriteria);

        var copy = reviewBetaCriteria.copy();

        assertThat(reviewBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewBetaCriteria = new ReviewBetaCriteria();

        assertThat(reviewBetaCriteria).hasToString("ReviewBetaCriteria{}");
    }

    private static void setAllFilters(ReviewBetaCriteria reviewBetaCriteria) {
        reviewBetaCriteria.id();
        reviewBetaCriteria.rating();
        reviewBetaCriteria.reviewDate();
        reviewBetaCriteria.productId();
        reviewBetaCriteria.tenantId();
        reviewBetaCriteria.distinct();
    }

    private static Condition<ReviewBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewBetaCriteria> copyFiltersAre(ReviewBetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
