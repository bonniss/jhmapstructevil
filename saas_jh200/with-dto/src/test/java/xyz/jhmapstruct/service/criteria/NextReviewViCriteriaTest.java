package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewViCriteriaTest {

    @Test
    void newNextReviewViCriteriaHasAllFiltersNullTest() {
        var nextReviewViCriteria = new NextReviewViCriteria();
        assertThat(nextReviewViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewViCriteria = new NextReviewViCriteria();

        setAllFilters(nextReviewViCriteria);

        assertThat(nextReviewViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewViCriteriaCopyCreatesNullFilterTest() {
        var nextReviewViCriteria = new NextReviewViCriteria();
        var copy = nextReviewViCriteria.copy();

        assertThat(nextReviewViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewViCriteria)
        );
    }

    @Test
    void nextReviewViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewViCriteria = new NextReviewViCriteria();
        setAllFilters(nextReviewViCriteria);

        var copy = nextReviewViCriteria.copy();

        assertThat(nextReviewViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewViCriteria = new NextReviewViCriteria();

        assertThat(nextReviewViCriteria).hasToString("NextReviewViCriteria{}");
    }

    private static void setAllFilters(NextReviewViCriteria nextReviewViCriteria) {
        nextReviewViCriteria.id();
        nextReviewViCriteria.rating();
        nextReviewViCriteria.reviewDate();
        nextReviewViCriteria.productId();
        nextReviewViCriteria.tenantId();
        nextReviewViCriteria.distinct();
    }

    private static Condition<NextReviewViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewViCriteria> copyFiltersAre(
        NextReviewViCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
