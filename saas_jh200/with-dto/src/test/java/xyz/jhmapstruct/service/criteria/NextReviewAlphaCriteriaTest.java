package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewAlphaCriteriaTest {

    @Test
    void newNextReviewAlphaCriteriaHasAllFiltersNullTest() {
        var nextReviewAlphaCriteria = new NextReviewAlphaCriteria();
        assertThat(nextReviewAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewAlphaCriteria = new NextReviewAlphaCriteria();

        setAllFilters(nextReviewAlphaCriteria);

        assertThat(nextReviewAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextReviewAlphaCriteria = new NextReviewAlphaCriteria();
        var copy = nextReviewAlphaCriteria.copy();

        assertThat(nextReviewAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewAlphaCriteria)
        );
    }

    @Test
    void nextReviewAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewAlphaCriteria = new NextReviewAlphaCriteria();
        setAllFilters(nextReviewAlphaCriteria);

        var copy = nextReviewAlphaCriteria.copy();

        assertThat(nextReviewAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewAlphaCriteria = new NextReviewAlphaCriteria();

        assertThat(nextReviewAlphaCriteria).hasToString("NextReviewAlphaCriteria{}");
    }

    private static void setAllFilters(NextReviewAlphaCriteria nextReviewAlphaCriteria) {
        nextReviewAlphaCriteria.id();
        nextReviewAlphaCriteria.rating();
        nextReviewAlphaCriteria.reviewDate();
        nextReviewAlphaCriteria.productId();
        nextReviewAlphaCriteria.tenantId();
        nextReviewAlphaCriteria.distinct();
    }

    private static Condition<NextReviewAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewAlphaCriteria> copyFiltersAre(
        NextReviewAlphaCriteria copy,
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
