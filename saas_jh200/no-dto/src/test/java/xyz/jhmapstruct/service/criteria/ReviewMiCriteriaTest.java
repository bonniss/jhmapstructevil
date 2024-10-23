package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewMiCriteriaTest {

    @Test
    void newReviewMiCriteriaHasAllFiltersNullTest() {
        var reviewMiCriteria = new ReviewMiCriteria();
        assertThat(reviewMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewMiCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewMiCriteria = new ReviewMiCriteria();

        setAllFilters(reviewMiCriteria);

        assertThat(reviewMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewMiCriteriaCopyCreatesNullFilterTest() {
        var reviewMiCriteria = new ReviewMiCriteria();
        var copy = reviewMiCriteria.copy();

        assertThat(reviewMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewMiCriteria)
        );
    }

    @Test
    void reviewMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewMiCriteria = new ReviewMiCriteria();
        setAllFilters(reviewMiCriteria);

        var copy = reviewMiCriteria.copy();

        assertThat(reviewMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewMiCriteria = new ReviewMiCriteria();

        assertThat(reviewMiCriteria).hasToString("ReviewMiCriteria{}");
    }

    private static void setAllFilters(ReviewMiCriteria reviewMiCriteria) {
        reviewMiCriteria.id();
        reviewMiCriteria.rating();
        reviewMiCriteria.reviewDate();
        reviewMiCriteria.productId();
        reviewMiCriteria.tenantId();
        reviewMiCriteria.distinct();
    }

    private static Condition<ReviewMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewMiCriteria> copyFiltersAre(ReviewMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
