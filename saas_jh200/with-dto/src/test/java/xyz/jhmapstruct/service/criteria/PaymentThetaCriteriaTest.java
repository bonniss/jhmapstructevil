package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentThetaCriteriaTest {

    @Test
    void newPaymentThetaCriteriaHasAllFiltersNullTest() {
        var paymentThetaCriteria = new PaymentThetaCriteria();
        assertThat(paymentThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentThetaCriteria = new PaymentThetaCriteria();

        setAllFilters(paymentThetaCriteria);

        assertThat(paymentThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentThetaCriteriaCopyCreatesNullFilterTest() {
        var paymentThetaCriteria = new PaymentThetaCriteria();
        var copy = paymentThetaCriteria.copy();

        assertThat(paymentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentThetaCriteria)
        );
    }

    @Test
    void paymentThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentThetaCriteria = new PaymentThetaCriteria();
        setAllFilters(paymentThetaCriteria);

        var copy = paymentThetaCriteria.copy();

        assertThat(paymentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentThetaCriteria = new PaymentThetaCriteria();

        assertThat(paymentThetaCriteria).hasToString("PaymentThetaCriteria{}");
    }

    private static void setAllFilters(PaymentThetaCriteria paymentThetaCriteria) {
        paymentThetaCriteria.id();
        paymentThetaCriteria.amount();
        paymentThetaCriteria.paymentDate();
        paymentThetaCriteria.paymentMethod();
        paymentThetaCriteria.tenantId();
        paymentThetaCriteria.distinct();
    }

    private static Condition<PaymentThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentThetaCriteria> copyFiltersAre(
        PaymentThetaCriteria copy,
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
