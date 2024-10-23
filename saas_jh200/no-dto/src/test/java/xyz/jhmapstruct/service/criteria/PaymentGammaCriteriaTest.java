package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentGammaCriteriaTest {

    @Test
    void newPaymentGammaCriteriaHasAllFiltersNullTest() {
        var paymentGammaCriteria = new PaymentGammaCriteria();
        assertThat(paymentGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentGammaCriteria = new PaymentGammaCriteria();

        setAllFilters(paymentGammaCriteria);

        assertThat(paymentGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentGammaCriteriaCopyCreatesNullFilterTest() {
        var paymentGammaCriteria = new PaymentGammaCriteria();
        var copy = paymentGammaCriteria.copy();

        assertThat(paymentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentGammaCriteria)
        );
    }

    @Test
    void paymentGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentGammaCriteria = new PaymentGammaCriteria();
        setAllFilters(paymentGammaCriteria);

        var copy = paymentGammaCriteria.copy();

        assertThat(paymentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentGammaCriteria = new PaymentGammaCriteria();

        assertThat(paymentGammaCriteria).hasToString("PaymentGammaCriteria{}");
    }

    private static void setAllFilters(PaymentGammaCriteria paymentGammaCriteria) {
        paymentGammaCriteria.id();
        paymentGammaCriteria.amount();
        paymentGammaCriteria.paymentDate();
        paymentGammaCriteria.paymentMethod();
        paymentGammaCriteria.tenantId();
        paymentGammaCriteria.distinct();
    }

    private static Condition<PaymentGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentGammaCriteria> copyFiltersAre(
        PaymentGammaCriteria copy,
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
