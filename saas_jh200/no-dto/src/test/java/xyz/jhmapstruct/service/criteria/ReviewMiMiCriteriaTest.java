package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReviewMiMiCriteriaTest {

    @Test
    void newReviewMiMiCriteriaHasAllFiltersNullTest() {
        var reviewMiMiCriteria = new ReviewMiMiCriteria();
        assertThat(reviewMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void reviewMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var reviewMiMiCriteria = new ReviewMiMiCriteria();

        setAllFilters(reviewMiMiCriteria);

        assertThat(reviewMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void reviewMiMiCriteriaCopyCreatesNullFilterTest() {
        var reviewMiMiCriteria = new ReviewMiMiCriteria();
        var copy = reviewMiMiCriteria.copy();

        assertThat(reviewMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(reviewMiMiCriteria)
        );
    }

    @Test
    void reviewMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reviewMiMiCriteria = new ReviewMiMiCriteria();
        setAllFilters(reviewMiMiCriteria);

        var copy = reviewMiMiCriteria.copy();

        assertThat(reviewMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(reviewMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reviewMiMiCriteria = new ReviewMiMiCriteria();

        assertThat(reviewMiMiCriteria).hasToString("ReviewMiMiCriteria{}");
    }

    private static void setAllFilters(ReviewMiMiCriteria reviewMiMiCriteria) {
        reviewMiMiCriteria.id();
        reviewMiMiCriteria.rating();
        reviewMiMiCriteria.reviewDate();
        reviewMiMiCriteria.productId();
        reviewMiMiCriteria.tenantId();
        reviewMiMiCriteria.distinct();
    }

    private static Condition<ReviewMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ReviewMiMiCriteria> copyFiltersAre(ReviewMiMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
