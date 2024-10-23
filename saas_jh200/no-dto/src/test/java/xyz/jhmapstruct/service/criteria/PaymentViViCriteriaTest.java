package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentViViCriteriaTest {

    @Test
    void newPaymentViViCriteriaHasAllFiltersNullTest() {
        var paymentViViCriteria = new PaymentViViCriteria();
        assertThat(paymentViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentViViCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentViViCriteria = new PaymentViViCriteria();

        setAllFilters(paymentViViCriteria);

        assertThat(paymentViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentViViCriteriaCopyCreatesNullFilterTest() {
        var paymentViViCriteria = new PaymentViViCriteria();
        var copy = paymentViViCriteria.copy();

        assertThat(paymentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentViViCriteria)
        );
    }

    @Test
    void paymentViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentViViCriteria = new PaymentViViCriteria();
        setAllFilters(paymentViViCriteria);

        var copy = paymentViViCriteria.copy();

        assertThat(paymentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentViViCriteria = new PaymentViViCriteria();

        assertThat(paymentViViCriteria).hasToString("PaymentViViCriteria{}");
    }

    private static void setAllFilters(PaymentViViCriteria paymentViViCriteria) {
        paymentViViCriteria.id();
        paymentViViCriteria.amount();
        paymentViViCriteria.paymentDate();
        paymentViViCriteria.paymentMethod();
        paymentViViCriteria.tenantId();
        paymentViViCriteria.distinct();
    }

    private static Condition<PaymentViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentViViCriteria> copyFiltersAre(PaymentViViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
