package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderGammaCriteriaTest {

    @Test
    void newOrderGammaCriteriaHasAllFiltersNullTest() {
        var orderGammaCriteria = new OrderGammaCriteria();
        assertThat(orderGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var orderGammaCriteria = new OrderGammaCriteria();

        setAllFilters(orderGammaCriteria);

        assertThat(orderGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderGammaCriteriaCopyCreatesNullFilterTest() {
        var orderGammaCriteria = new OrderGammaCriteria();
        var copy = orderGammaCriteria.copy();

        assertThat(orderGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderGammaCriteria)
        );
    }

    @Test
    void orderGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderGammaCriteria = new OrderGammaCriteria();
        setAllFilters(orderGammaCriteria);

        var copy = orderGammaCriteria.copy();

        assertThat(orderGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderGammaCriteria = new OrderGammaCriteria();

        assertThat(orderGammaCriteria).hasToString("OrderGammaCriteria{}");
    }

    private static void setAllFilters(OrderGammaCriteria orderGammaCriteria) {
        orderGammaCriteria.id();
        orderGammaCriteria.orderDate();
        orderGammaCriteria.totalPrice();
        orderGammaCriteria.status();
        orderGammaCriteria.productsId();
        orderGammaCriteria.paymentId();
        orderGammaCriteria.shipmentId();
        orderGammaCriteria.tenantId();
        orderGammaCriteria.customerId();
        orderGammaCriteria.distinct();
    }

    private static Condition<OrderGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderGammaCriteria> copyFiltersAre(OrderGammaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
