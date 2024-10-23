package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentBetaCriteriaTest {

    @Test
    void newPaymentBetaCriteriaHasAllFiltersNullTest() {
        var paymentBetaCriteria = new PaymentBetaCriteria();
        assertThat(paymentBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentBetaCriteria = new PaymentBetaCriteria();

        setAllFilters(paymentBetaCriteria);

        assertThat(paymentBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentBetaCriteriaCopyCreatesNullFilterTest() {
        var paymentBetaCriteria = new PaymentBetaCriteria();
        var copy = paymentBetaCriteria.copy();

        assertThat(paymentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentBetaCriteria)
        );
    }

    @Test
    void paymentBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentBetaCriteria = new PaymentBetaCriteria();
        setAllFilters(paymentBetaCriteria);

        var copy = paymentBetaCriteria.copy();

        assertThat(paymentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentBetaCriteria = new PaymentBetaCriteria();

        assertThat(paymentBetaCriteria).hasToString("PaymentBetaCriteria{}");
    }

    private static void setAllFilters(PaymentBetaCriteria paymentBetaCriteria) {
        paymentBetaCriteria.id();
        paymentBetaCriteria.amount();
        paymentBetaCriteria.paymentDate();
        paymentBetaCriteria.paymentMethod();
        paymentBetaCriteria.tenantId();
        paymentBetaCriteria.distinct();
    }

    private static Condition<PaymentBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentBetaCriteria> copyFiltersAre(PaymentBetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
