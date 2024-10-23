package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewCriteriaTest {

    @Test
    void newNextReviewCriteriaHasAllFiltersNullTest() {
        var nextReviewCriteria = new NextReviewCriteria();
        assertThat(nextReviewCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewCriteria = new NextReviewCriteria();

        setAllFilters(nextReviewCriteria);

        assertThat(nextReviewCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewCriteriaCopyCreatesNullFilterTest() {
        var nextReviewCriteria = new NextReviewCriteria();
        var copy = nextReviewCriteria.copy();

        assertThat(nextReviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewCriteria)
        );
    }

    @Test
    void nextReviewCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewCriteria = new NextReviewCriteria();
        setAllFilters(nextReviewCriteria);

        var copy = nextReviewCriteria.copy();

        assertThat(nextReviewCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewCriteria = new NextReviewCriteria();

        assertThat(nextReviewCriteria).hasToString("NextReviewCriteria{}");
    }

    private static void setAllFilters(NextReviewCriteria nextReviewCriteria) {
        nextReviewCriteria.id();
        nextReviewCriteria.rating();
        nextReviewCriteria.reviewDate();
        nextReviewCriteria.productId();
        nextReviewCriteria.tenantId();
        nextReviewCriteria.distinct();
    }

    private static Condition<NextReviewCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewCriteria> copyFiltersAre(NextReviewCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
