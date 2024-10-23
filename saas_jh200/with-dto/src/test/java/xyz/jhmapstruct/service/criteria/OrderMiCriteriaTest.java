package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderMiCriteriaTest {

    @Test
    void newOrderMiCriteriaHasAllFiltersNullTest() {
        var orderMiCriteria = new OrderMiCriteria();
        assertThat(orderMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderMiCriteriaFluentMethodsCreatesFiltersTest() {
        var orderMiCriteria = new OrderMiCriteria();

        setAllFilters(orderMiCriteria);

        assertThat(orderMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderMiCriteriaCopyCreatesNullFilterTest() {
        var orderMiCriteria = new OrderMiCriteria();
        var copy = orderMiCriteria.copy();

        assertThat(orderMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderMiCriteria)
        );
    }

    @Test
    void orderMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderMiCriteria = new OrderMiCriteria();
        setAllFilters(orderMiCriteria);

        var copy = orderMiCriteria.copy();

        assertThat(orderMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderMiCriteria = new OrderMiCriteria();

        assertThat(orderMiCriteria).hasToString("OrderMiCriteria{}");
    }

    private static void setAllFilters(OrderMiCriteria orderMiCriteria) {
        orderMiCriteria.id();
        orderMiCriteria.orderDate();
        orderMiCriteria.totalPrice();
        orderMiCriteria.status();
        orderMiCriteria.productsId();
        orderMiCriteria.paymentId();
        orderMiCriteria.shipmentId();
        orderMiCriteria.tenantId();
        orderMiCriteria.customerId();
        orderMiCriteria.distinct();
    }

    private static Condition<OrderMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderMiCriteria> copyFiltersAre(OrderMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
