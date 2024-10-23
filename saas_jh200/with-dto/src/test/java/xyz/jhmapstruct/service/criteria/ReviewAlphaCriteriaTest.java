package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewAlphaCriteriaTest {

    @Test
    void newReviewAlphaCriteriaHasAllFiltersNullTest() {
        var reviewAlphaCriteria = new ReviewAlphaCriteria();
        assertThat(reviewAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewAlphaCriteria = new ReviewAlphaCriteria();

        setAllFilters(reviewAlphaCriteria);

        assertThat(reviewAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewAlphaCriteriaCopyCreatesNullFilterTest() {
        var reviewAlphaCriteria = new ReviewAlphaCriteria();
        var copy = reviewAlphaCriteria.copy();

        assertThat(reviewAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewAlphaCriteria)
        );
    }

    @Test
    void reviewAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewAlphaCriteria = new ReviewAlphaCriteria();
        setAllFilters(reviewAlphaCriteria);

        var copy = reviewAlphaCriteria.copy();

        assertThat(reviewAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewAlphaCriteria = new ReviewAlphaCriteria();

        assertThat(reviewAlphaCriteria).hasToString("ReviewAlphaCriteria{}");
    }

    private static void setAllFilters(ReviewAlphaCriteria reviewAlphaCriteria) {
        reviewAlphaCriteria.id();
        reviewAlphaCriteria.rating();
        reviewAlphaCriteria.reviewDate();
        reviewAlphaCriteria.productId();
        reviewAlphaCriteria.tenantId();
        reviewAlphaCriteria.distinct();
    }

    private static Condition<ReviewAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewAlphaCriteria> copyFiltersAre(ReviewAlphaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
