package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderSigmaCriteriaTest {

    @Test
    void newOrderSigmaCriteriaHasAllFiltersNullTest() {
        var orderSigmaCriteria = new OrderSigmaCriteria();
        assertThat(orderSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var orderSigmaCriteria = new OrderSigmaCriteria();

        setAllFilters(orderSigmaCriteria);

        assertThat(orderSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderSigmaCriteriaCopyCreatesNullFilterTest() {
        var orderSigmaCriteria = new OrderSigmaCriteria();
        var copy = orderSigmaCriteria.copy();

        assertThat(orderSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderSigmaCriteria)
        );
    }

    @Test
    void orderSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderSigmaCriteria = new OrderSigmaCriteria();
        setAllFilters(orderSigmaCriteria);

        var copy = orderSigmaCriteria.copy();

        assertThat(orderSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderSigmaCriteria = new OrderSigmaCriteria();

        assertThat(orderSigmaCriteria).hasToString("OrderSigmaCriteria{}");
    }

    private static void setAllFilters(OrderSigmaCriteria orderSigmaCriteria) {
        orderSigmaCriteria.id();
        orderSigmaCriteria.orderDate();
        orderSigmaCriteria.totalPrice();
        orderSigmaCriteria.status();
        orderSigmaCriteria.productsId();
        orderSigmaCriteria.paymentId();
        orderSigmaCriteria.shipmentId();
        orderSigmaCriteria.tenantId();
        orderSigmaCriteria.customerId();
        orderSigmaCriteria.distinct();
    }

    private static Condition<OrderSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderSigmaCriteria> copyFiltersAre(OrderSigmaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
