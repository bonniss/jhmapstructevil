package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewSigmaCriteriaTest {

    @Test
    void newNextReviewSigmaCriteriaHasAllFiltersNullTest() {
        var nextReviewSigmaCriteria = new NextReviewSigmaCriteria();
        assertThat(nextReviewSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewSigmaCriteria = new NextReviewSigmaCriteria();

        setAllFilters(nextReviewSigmaCriteria);

        assertThat(nextReviewSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextReviewSigmaCriteria = new NextReviewSigmaCriteria();
        var copy = nextReviewSigmaCriteria.copy();

        assertThat(nextReviewSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewSigmaCriteria)
        );
    }

    @Test
    void nextReviewSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewSigmaCriteria = new NextReviewSigmaCriteria();
        setAllFilters(nextReviewSigmaCriteria);

        var copy = nextReviewSigmaCriteria.copy();

        assertThat(nextReviewSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewSigmaCriteria = new NextReviewSigmaCriteria();

        assertThat(nextReviewSigmaCriteria).hasToString("NextReviewSigmaCriteria{}");
    }

    private static void setAllFilters(NextReviewSigmaCriteria nextReviewSigmaCriteria) {
        nextReviewSigmaCriteria.id();
        nextReviewSigmaCriteria.rating();
        nextReviewSigmaCriteria.reviewDate();
        nextReviewSigmaCriteria.productId();
        nextReviewSigmaCriteria.tenantId();
        nextReviewSigmaCriteria.distinct();
    }

    private static Condition<NextReviewSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewSigmaCriteria> copyFiltersAre(
        NextReviewSigmaCriteria copy,
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
