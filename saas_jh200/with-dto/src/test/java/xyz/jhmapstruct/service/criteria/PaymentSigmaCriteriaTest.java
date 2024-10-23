package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentSigmaCriteriaTest {

    @Test
    void newPaymentSigmaCriteriaHasAllFiltersNullTest() {
        var paymentSigmaCriteria = new PaymentSigmaCriteria();
        assertThat(paymentSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentSigmaCriteria = new PaymentSigmaCriteria();

        setAllFilters(paymentSigmaCriteria);

        assertThat(paymentSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentSigmaCriteriaCopyCreatesNullFilterTest() {
        var paymentSigmaCriteria = new PaymentSigmaCriteria();
        var copy = paymentSigmaCriteria.copy();

        assertThat(paymentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentSigmaCriteria)
        );
    }

    @Test
    void paymentSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentSigmaCriteria = new PaymentSigmaCriteria();
        setAllFilters(paymentSigmaCriteria);

        var copy = paymentSigmaCriteria.copy();

        assertThat(paymentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentSigmaCriteria = new PaymentSigmaCriteria();

        assertThat(paymentSigmaCriteria).hasToString("PaymentSigmaCriteria{}");
    }

    private static void setAllFilters(PaymentSigmaCriteria paymentSigmaCriteria) {
        paymentSigmaCriteria.id();
        paymentSigmaCriteria.amount();
        paymentSigmaCriteria.paymentDate();
        paymentSigmaCriteria.paymentMethod();
        paymentSigmaCriteria.tenantId();
        paymentSigmaCriteria.distinct();
    }

    private static Condition<PaymentSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentSigmaCriteria> copyFiltersAre(
        PaymentSigmaCriteria copy,
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
