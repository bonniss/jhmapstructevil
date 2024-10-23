package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderThetaCriteriaTest {

    @Test
    void newOrderThetaCriteriaHasAllFiltersNullTest() {
        var orderThetaCriteria = new OrderThetaCriteria();
        assertThat(orderThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var orderThetaCriteria = new OrderThetaCriteria();

        setAllFilters(orderThetaCriteria);

        assertThat(orderThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderThetaCriteriaCopyCreatesNullFilterTest() {
        var orderThetaCriteria = new OrderThetaCriteria();
        var copy = orderThetaCriteria.copy();

        assertThat(orderThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderThetaCriteria)
        );
    }

    @Test
    void orderThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderThetaCriteria = new OrderThetaCriteria();
        setAllFilters(orderThetaCriteria);

        var copy = orderThetaCriteria.copy();

        assertThat(orderThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderThetaCriteria = new OrderThetaCriteria();

        assertThat(orderThetaCriteria).hasToString("OrderThetaCriteria{}");
    }

    private static void setAllFilters(OrderThetaCriteria orderThetaCriteria) {
        orderThetaCriteria.id();
        orderThetaCriteria.orderDate();
        orderThetaCriteria.totalPrice();
        orderThetaCriteria.status();
        orderThetaCriteria.productsId();
        orderThetaCriteria.paymentId();
        orderThetaCriteria.shipmentId();
        orderThetaCriteria.tenantId();
        orderThetaCriteria.customerId();
        orderThetaCriteria.distinct();
    }

    private static Condition<OrderThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderThetaCriteria> copyFiltersAre(OrderThetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
