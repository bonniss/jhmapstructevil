package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewThetaCriteriaTest {

    @Test
    void newNextReviewThetaCriteriaHasAllFiltersNullTest() {
        var nextReviewThetaCriteria = new NextReviewThetaCriteria();
        assertThat(nextReviewThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewThetaCriteria = new NextReviewThetaCriteria();

        setAllFilters(nextReviewThetaCriteria);

        assertThat(nextReviewThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewThetaCriteriaCopyCreatesNullFilterTest() {
        var nextReviewThetaCriteria = new NextReviewThetaCriteria();
        var copy = nextReviewThetaCriteria.copy();

        assertThat(nextReviewThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewThetaCriteria)
        );
    }

    @Test
    void nextReviewThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewThetaCriteria = new NextReviewThetaCriteria();
        setAllFilters(nextReviewThetaCriteria);

        var copy = nextReviewThetaCriteria.copy();

        assertThat(nextReviewThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewThetaCriteria = new NextReviewThetaCriteria();

        assertThat(nextReviewThetaCriteria).hasToString("NextReviewThetaCriteria{}");
    }

    private static void setAllFilters(NextReviewThetaCriteria nextReviewThetaCriteria) {
        nextReviewThetaCriteria.id();
        nextReviewThetaCriteria.rating();
        nextReviewThetaCriteria.reviewDate();
        nextReviewThetaCriteria.productId();
        nextReviewThetaCriteria.tenantId();
        nextReviewThetaCriteria.distinct();
    }

    private static Condition<NextReviewThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewThetaCriteria> copyFiltersAre(
        NextReviewThetaCriteria copy,
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
