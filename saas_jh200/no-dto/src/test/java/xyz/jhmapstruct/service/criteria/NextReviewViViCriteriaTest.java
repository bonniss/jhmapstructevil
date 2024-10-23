package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewViViCriteriaTest {

    @Test
    void newNextReviewViViCriteriaHasAllFiltersNullTest() {
        var nextReviewViViCriteria = new NextReviewViViCriteria();
        assertThat(nextReviewViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewViViCriteria = new NextReviewViViCriteria();

        setAllFilters(nextReviewViViCriteria);

        assertThat(nextReviewViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewViViCriteriaCopyCreatesNullFilterTest() {
        var nextReviewViViCriteria = new NextReviewViViCriteria();
        var copy = nextReviewViViCriteria.copy();

        assertThat(nextReviewViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewViViCriteria)
        );
    }

    @Test
    void nextReviewViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewViViCriteria = new NextReviewViViCriteria();
        setAllFilters(nextReviewViViCriteria);

        var copy = nextReviewViViCriteria.copy();

        assertThat(nextReviewViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewViViCriteria = new NextReviewViViCriteria();

        assertThat(nextReviewViViCriteria).hasToString("NextReviewViViCriteria{}");
    }

    private static void setAllFilters(NextReviewViViCriteria nextReviewViViCriteria) {
        nextReviewViViCriteria.id();
        nextReviewViViCriteria.rating();
        nextReviewViViCriteria.reviewDate();
        nextReviewViViCriteria.productId();
        nextReviewViViCriteria.tenantId();
        nextReviewViViCriteria.distinct();
    }

    private static Condition<NextReviewViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewViViCriteria> copyFiltersAre(
        NextReviewViViCriteria copy,
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
