package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderMiMiCriteriaTest {

    @Test
    void newOrderMiMiCriteriaHasAllFiltersNullTest() {
        var orderMiMiCriteria = new OrderMiMiCriteria();
        assertThat(orderMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var orderMiMiCriteria = new OrderMiMiCriteria();

        setAllFilters(orderMiMiCriteria);

        assertThat(orderMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderMiMiCriteriaCopyCreatesNullFilterTest() {
        var orderMiMiCriteria = new OrderMiMiCriteria();
        var copy = orderMiMiCriteria.copy();

        assertThat(orderMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderMiMiCriteria)
        );
    }

    @Test
    void orderMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderMiMiCriteria = new OrderMiMiCriteria();
        setAllFilters(orderMiMiCriteria);

        var copy = orderMiMiCriteria.copy();

        assertThat(orderMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderMiMiCriteria = new OrderMiMiCriteria();

        assertThat(orderMiMiCriteria).hasToString("OrderMiMiCriteria{}");
    }

    private static void setAllFilters(OrderMiMiCriteria orderMiMiCriteria) {
        orderMiMiCriteria.id();
        orderMiMiCriteria.orderDate();
        orderMiMiCriteria.totalPrice();
        orderMiMiCriteria.status();
        orderMiMiCriteria.productsId();
        orderMiMiCriteria.paymentId();
        orderMiMiCriteria.shipmentId();
        orderMiMiCriteria.tenantId();
        orderMiMiCriteria.customerId();
        orderMiMiCriteria.distinct();
    }

    private static Condition<OrderMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderMiMiCriteria> copyFiltersAre(OrderMiMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
