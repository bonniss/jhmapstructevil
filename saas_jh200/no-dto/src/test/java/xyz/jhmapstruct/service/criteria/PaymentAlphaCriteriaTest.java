package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentAlphaCriteriaTest {

    @Test
    void newPaymentAlphaCriteriaHasAllFiltersNullTest() {
        var paymentAlphaCriteria = new PaymentAlphaCriteria();
        assertThat(paymentAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentAlphaCriteria = new PaymentAlphaCriteria();

        setAllFilters(paymentAlphaCriteria);

        assertThat(paymentAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentAlphaCriteriaCopyCreatesNullFilterTest() {
        var paymentAlphaCriteria = new PaymentAlphaCriteria();
        var copy = paymentAlphaCriteria.copy();

        assertThat(paymentAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentAlphaCriteria)
        );
    }

    @Test
    void paymentAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentAlphaCriteria = new PaymentAlphaCriteria();
        setAllFilters(paymentAlphaCriteria);

        var copy = paymentAlphaCriteria.copy();

        assertThat(paymentAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentAlphaCriteria = new PaymentAlphaCriteria();

        assertThat(paymentAlphaCriteria).hasToString("PaymentAlphaCriteria{}");
    }

    private static void setAllFilters(PaymentAlphaCriteria paymentAlphaCriteria) {
        paymentAlphaCriteria.id();
        paymentAlphaCriteria.amount();
        paymentAlphaCriteria.paymentDate();
        paymentAlphaCriteria.paymentMethod();
        paymentAlphaCriteria.tenantId();
        paymentAlphaCriteria.distinct();
    }

    private static Condition<PaymentAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentAlphaCriteria> copyFiltersAre(
        PaymentAlphaCriteria copy,
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