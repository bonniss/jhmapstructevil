package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewMiCriteriaTest {

    @Test
    void newNextReviewMiCriteriaHasAllFiltersNullTest() {
        var nextReviewMiCriteria = new NextReviewMiCriteria();
        assertThat(nextReviewMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewMiCriteria = new NextReviewMiCriteria();

        setAllFilters(nextReviewMiCriteria);

        assertThat(nextReviewMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewMiCriteriaCopyCreatesNullFilterTest() {
        var nextReviewMiCriteria = new NextReviewMiCriteria();
        var copy = nextReviewMiCriteria.copy();

        assertThat(nextReviewMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewMiCriteria)
        );
    }

    @Test
    void nextReviewMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewMiCriteria = new NextReviewMiCriteria();
        setAllFilters(nextReviewMiCriteria);

        var copy = nextReviewMiCriteria.copy();

        assertThat(nextReviewMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewMiCriteria = new NextReviewMiCriteria();

        assertThat(nextReviewMiCriteria).hasToString("NextReviewMiCriteria{}");
    }

    private static void setAllFilters(NextReviewMiCriteria nextReviewMiCriteria) {
        nextReviewMiCriteria.id();
        nextReviewMiCriteria.rating();
        nextReviewMiCriteria.reviewDate();
        nextReviewMiCriteria.productId();
        nextReviewMiCriteria.tenantId();
        nextReviewMiCriteria.distinct();
    }

    private static Condition<NextReviewMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewMiCriteria> copyFiltersAre(
        NextReviewMiCriteria copy,
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
