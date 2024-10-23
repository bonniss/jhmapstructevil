package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewBetaCriteriaTest {

    @Test
    void newNextReviewBetaCriteriaHasAllFiltersNullTest() {
        var nextReviewBetaCriteria = new NextReviewBetaCriteria();
        assertThat(nextReviewBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewBetaCriteria = new NextReviewBetaCriteria();

        setAllFilters(nextReviewBetaCriteria);

        assertThat(nextReviewBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewBetaCriteriaCopyCreatesNullFilterTest() {
        var nextReviewBetaCriteria = new NextReviewBetaCriteria();
        var copy = nextReviewBetaCriteria.copy();

        assertThat(nextReviewBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewBetaCriteria)
        );
    }

    @Test
    void nextReviewBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewBetaCriteria = new NextReviewBetaCriteria();
        setAllFilters(nextReviewBetaCriteria);

        var copy = nextReviewBetaCriteria.copy();

        assertThat(nextReviewBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewBetaCriteria = new NextReviewBetaCriteria();

        assertThat(nextReviewBetaCriteria).hasToString("NextReviewBetaCriteria{}");
    }

    private static void setAllFilters(NextReviewBetaCriteria nextReviewBetaCriteria) {
        nextReviewBetaCriteria.id();
        nextReviewBetaCriteria.rating();
        nextReviewBetaCriteria.reviewDate();
        nextReviewBetaCriteria.productId();
        nextReviewBetaCriteria.tenantId();
        nextReviewBetaCriteria.distinct();
    }

    private static Condition<NextReviewBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewBetaCriteria> copyFiltersAre(
        NextReviewBetaCriteria copy,
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
