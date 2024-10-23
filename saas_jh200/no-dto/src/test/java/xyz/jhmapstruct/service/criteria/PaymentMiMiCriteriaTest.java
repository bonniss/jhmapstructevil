package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaymentMiMiCriteriaTest {

    @Test
    void newPaymentMiMiCriteriaHasAllFiltersNullTest() {
        var paymentMiMiCriteria = new PaymentMiMiCriteria();
        assertThat(paymentMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void paymentMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var paymentMiMiCriteria = new PaymentMiMiCriteria();

        setAllFilters(paymentMiMiCriteria);

        assertThat(paymentMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void paymentMiMiCriteriaCopyCreatesNullFilterTest() {
        var paymentMiMiCriteria = new PaymentMiMiCriteria();
        var copy = paymentMiMiCriteria.copy();

        assertThat(paymentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(paymentMiMiCriteria)
        );
    }

    @Test
    void paymentMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paymentMiMiCriteria = new PaymentMiMiCriteria();
        setAllFilters(paymentMiMiCriteria);

        var copy = paymentMiMiCriteria.copy();

        assertThat(paymentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(paymentMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paymentMiMiCriteria = new PaymentMiMiCriteria();

        assertThat(paymentMiMiCriteria).hasToString("PaymentMiMiCriteria{}");
    }

    private static void setAllFilters(PaymentMiMiCriteria paymentMiMiCriteria) {
        paymentMiMiCriteria.id();
        paymentMiMiCriteria.amount();
        paymentMiMiCriteria.paymentDate();
        paymentMiMiCriteria.paymentMethod();
        paymentMiMiCriteria.tenantId();
        paymentMiMiCriteria.distinct();
    }

    private static Condition<PaymentMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<PaymentMiMiCriteria> copyFiltersAre(PaymentMiMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
