package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentMiCriteriaTest {

    @Test
    void newPaymentMiCriteriaHasAllFiltersNullTest() {
        var paymentMiCriteria = new PaymentMiCriteria();
        assertThat(paymentMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentMiCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentMiCriteria = new PaymentMiCriteria();

        setAllFilters(paymentMiCriteria);

        assertThat(paymentMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentMiCriteriaCopyCreatesNullFilterTest() {
        var paymentMiCriteria = new PaymentMiCriteria();
        var copy = paymentMiCriteria.copy();

        assertThat(paymentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentMiCriteria)
        );
    }

    @Test
    void paymentMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentMiCriteria = new PaymentMiCriteria();
        setAllFilters(paymentMiCriteria);

        var copy = paymentMiCriteria.copy();

        assertThat(paymentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentMiCriteria = new PaymentMiCriteria();

        assertThat(paymentMiCriteria).hasToString("PaymentMiCriteria{}");
    }

    private static void setAllFilters(PaymentMiCriteria paymentMiCriteria) {
        paymentMiCriteria.id();
        paymentMiCriteria.amount();
        paymentMiCriteria.paymentDate();
        paymentMiCriteria.paymentMethod();
        paymentMiCriteria.tenantId();
        paymentMiCriteria.distinct();
    }

    private static Condition<PaymentMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentMiCriteria> copyFiltersAre(PaymentMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
