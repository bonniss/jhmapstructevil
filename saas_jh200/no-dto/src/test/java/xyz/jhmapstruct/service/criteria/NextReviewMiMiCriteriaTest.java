package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewMiMiCriteriaTest {

    @Test
    void newNextReviewMiMiCriteriaHasAllFiltersNullTest() {
        var nextReviewMiMiCriteria = new NextReviewMiMiCriteria();
        assertThat(nextReviewMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewMiMiCriteria = new NextReviewMiMiCriteria();

        setAllFilters(nextReviewMiMiCriteria);

        assertThat(nextReviewMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextReviewMiMiCriteria = new NextReviewMiMiCriteria();
        var copy = nextReviewMiMiCriteria.copy();

        assertThat(nextReviewMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewMiMiCriteria)
        );
    }

    @Test
    void nextReviewMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewMiMiCriteria = new NextReviewMiMiCriteria();
        setAllFilters(nextReviewMiMiCriteria);

        var copy = nextReviewMiMiCriteria.copy();

        assertThat(nextReviewMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewMiMiCriteria = new NextReviewMiMiCriteria();

        assertThat(nextReviewMiMiCriteria).hasToString("NextReviewMiMiCriteria{}");
    }

    private static void setAllFilters(NextReviewMiMiCriteria nextReviewMiMiCriteria) {
        nextReviewMiMiCriteria.id();
        nextReviewMiMiCriteria.rating();
        nextReviewMiMiCriteria.reviewDate();
        nextReviewMiMiCriteria.productId();
        nextReviewMiMiCriteria.tenantId();
        nextReviewMiMiCriteria.distinct();
    }

    private static Condition<NextReviewMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewMiMiCriteria> copyFiltersAre(
        NextReviewMiMiCriteria copy,
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
