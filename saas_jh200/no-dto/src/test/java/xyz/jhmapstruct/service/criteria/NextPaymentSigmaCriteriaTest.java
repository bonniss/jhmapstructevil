package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextPaymentSigmaCriteriaTest {

    @Test
    void newNextPaymentSigmaCriteriaHasAllFiltersNullTest() {
        var nextPaymentSigmaCriteria = new NextPaymentSigmaCriteria();
        assertThat(nextPaymentSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextPaymentSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextPaymentSigmaCriteria = new NextPaymentSigmaCriteria();

        setAllFilters(nextPaymentSigmaCriteria);

        assertThat(nextPaymentSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextPaymentSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextPaymentSigmaCriteria = new NextPaymentSigmaCriteria();
        var copy = nextPaymentSigmaCriteria.copy();

        assertThat(nextPaymentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentSigmaCriteria)
        );
    }

    @Test
    void nextPaymentSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextPaymentSigmaCriteria = new NextPaymentSigmaCriteria();
        setAllFilters(nextPaymentSigmaCriteria);

        var copy = nextPaymentSigmaCriteria.copy();

        assertThat(nextPaymentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextPaymentSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextPaymentSigmaCriteria = new NextPaymentSigmaCriteria();

        assertThat(nextPaymentSigmaCriteria).hasToString("NextPaymentSigmaCriteria{}");
    }

    private static void setAllFilters(NextPaymentSigmaCriteria nextPaymentSigmaCriteria) {
        nextPaymentSigmaCriteria.id();
        nextPaymentSigmaCriteria.amount();
        nextPaymentSigmaCriteria.paymentDate();
        nextPaymentSigmaCriteria.paymentMethod();
        nextPaymentSigmaCriteria.tenantId();
        nextPaymentSigmaCriteria.distinct();
    }

    private static Condition<NextPaymentSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getPaymentMethod()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextPaymentSigmaCriteria> copyFiltersAre(
        NextPaymentSigmaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getPaymentDate(), copy.getPaymentDate()) &&
                condition.apply(criteria.getPaymentMethod(), copy.getPaymentMethod()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
