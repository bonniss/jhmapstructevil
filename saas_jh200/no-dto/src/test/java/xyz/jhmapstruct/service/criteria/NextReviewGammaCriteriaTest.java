package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextReviewGammaCriteriaTest {

    @Test
    void newNextReviewGammaCriteriaHasAllFiltersNullTest() {
        var nextReviewGammaCriteria = new NextReviewGammaCriteria();
        assertThat(nextReviewGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextReviewGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextReviewGammaCriteria = new NextReviewGammaCriteria();

        setAllFilters(nextReviewGammaCriteria);

        assertThat(nextReviewGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextReviewGammaCriteriaCopyCreatesNullFilterTest() {
        var nextReviewGammaCriteria = new NextReviewGammaCriteria();
        var copy = nextReviewGammaCriteria.copy();

        assertThat(nextReviewGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewGammaCriteria)
        );
    }

    @Test
    void nextReviewGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextReviewGammaCriteria = new NextReviewGammaCriteria();
        setAllFilters(nextReviewGammaCriteria);

        var copy = nextReviewGammaCriteria.copy();

        assertThat(nextReviewGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextReviewGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextReviewGammaCriteria = new NextReviewGammaCriteria();

        assertThat(nextReviewGammaCriteria).hasToString("NextReviewGammaCriteria{}");
    }

    private static void setAllFilters(NextReviewGammaCriteria nextReviewGammaCriteria) {
        nextReviewGammaCriteria.id();
        nextReviewGammaCriteria.rating();
        nextReviewGammaCriteria.reviewDate();
        nextReviewGammaCriteria.productId();
        nextReviewGammaCriteria.tenantId();
        nextReviewGammaCriteria.distinct();
    }

    private static Condition<NextReviewGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextReviewGammaCriteria> copyFiltersAre(
        NextReviewGammaCriteria copy,
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