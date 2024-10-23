package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentViCriteriaTest {

    @Test
    void newPaymentViCriteriaHasAllFiltersNullTest() {
        var paymentViCriteria = new PaymentViCriteria();
        assertThat(paymentViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentViCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentViCriteria = new PaymentViCriteria();

        setAllFilters(paymentViCriteria);

        assertThat(paymentViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentViCriteriaCopyCreatesNullFilterTest() {
        var paymentViCriteria = new PaymentViCriteria();
        var copy = paymentViCriteria.copy();

        assertThat(paymentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentViCriteria)
        );
    }

    @Test
    void paymentViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentViCriteria = new PaymentViCriteria();
        setAllFilters(paymentViCriteria);

        var copy = paymentViCriteria.copy();

        assertThat(paymentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentViCriteria = new PaymentViCriteria();

        assertThat(paymentViCriteria).hasToString("PaymentViCriteria{}");
    }

    private static void setAllFilters(PaymentViCriteria paymentViCriteria) {
        paymentViCriteria.id();
        paymentViCriteria.amount();
        paymentViCriteria.paymentDate();
        paymentViCriteria.paymentMethod();
        paymentViCriteria.tenantId();
        paymentViCriteria.distinct();
    }

    private static Condition<PaymentViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentViCriteria> copyFiltersAre(PaymentViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
