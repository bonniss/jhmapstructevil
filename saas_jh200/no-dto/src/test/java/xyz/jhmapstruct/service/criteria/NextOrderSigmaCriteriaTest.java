package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderSigmaCriteriaTest {

    @Test
    void newNextOrderSigmaCriteriaHasAllFiltersNullTest() {
        var nextOrderSigmaCriteria = new NextOrderSigmaCriteria();
        assertThat(nextOrderSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderSigmaCriteria = new NextOrderSigmaCriteria();

        setAllFilters(nextOrderSigmaCriteria);

        assertThat(nextOrderSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderSigmaCriteriaCopyCreatesNullFilterTest() {
        var nextOrderSigmaCriteria = new NextOrderSigmaCriteria();
        var copy = nextOrderSigmaCriteria.copy();

        assertThat(nextOrderSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderSigmaCriteria)
        );
    }

    @Test
    void nextOrderSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderSigmaCriteria = new NextOrderSigmaCriteria();
        setAllFilters(nextOrderSigmaCriteria);

        var copy = nextOrderSigmaCriteria.copy();

        assertThat(nextOrderSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderSigmaCriteria = new NextOrderSigmaCriteria();

        assertThat(nextOrderSigmaCriteria).hasToString("NextOrderSigmaCriteria{}");
    }

    private static void setAllFilters(NextOrderSigmaCriteria nextOrderSigmaCriteria) {
        nextOrderSigmaCriteria.id();
        nextOrderSigmaCriteria.orderDate();
        nextOrderSigmaCriteria.totalPrice();
        nextOrderSigmaCriteria.status();
        nextOrderSigmaCriteria.productsId();
        nextOrderSigmaCriteria.paymentId();
        nextOrderSigmaCriteria.shipmentId();
        nextOrderSigmaCriteria.tenantId();
        nextOrderSigmaCriteria.customerId();
        nextOrderSigmaCriteria.distinct();
    }

    private static Condition<NextOrderSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderDate()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getProductsId()) &&
                condition.apply(criteria.getPaymentId()) &&
                condition.apply(criteria.getShipmentId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NextOrderSigmaCriteria> copyFiltersAre(
        NextOrderSigmaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderDate(), copy.getOrderDate()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getProductsId(), copy.getProductsId()) &&
                condition.apply(criteria.getPaymentId(), copy.getPaymentId()) &&
                condition.apply(criteria.getShipmentId(), copy.getShipmentId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
