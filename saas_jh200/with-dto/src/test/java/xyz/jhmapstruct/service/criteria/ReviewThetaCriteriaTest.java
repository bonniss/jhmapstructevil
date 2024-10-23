package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewThetaCriteriaTest {

    @Test
    void newReviewThetaCriteriaHasAllFiltersNullTest() {
        var reviewThetaCriteria = new ReviewThetaCriteria();
        assertThat(reviewThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewThetaCriteria = new ReviewThetaCriteria();

        setAllFilters(reviewThetaCriteria);

        assertThat(reviewThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewThetaCriteriaCopyCreatesNullFilterTest() {
        var reviewThetaCriteria = new ReviewThetaCriteria();
        var copy = reviewThetaCriteria.copy();

        assertThat(reviewThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewThetaCriteria)
        );
    }

    @Test
    void reviewThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewThetaCriteria = new ReviewThetaCriteria();
        setAllFilters(reviewThetaCriteria);

        var copy = reviewThetaCriteria.copy();

        assertThat(reviewThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewThetaCriteria = new ReviewThetaCriteria();

        assertThat(reviewThetaCriteria).hasToString("ReviewThetaCriteria{}");
    }

    private static void setAllFilters(ReviewThetaCriteria reviewThetaCriteria) {
        reviewThetaCriteria.id();
        reviewThetaCriteria.rating();
        reviewThetaCriteria.reviewDate();
        reviewThetaCriteria.productId();
        reviewThetaCriteria.tenantId();
        reviewThetaCriteria.distinct();
    }

    private static Condition<ReviewThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewThetaCriteria> copyFiltersAre(ReviewThetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
