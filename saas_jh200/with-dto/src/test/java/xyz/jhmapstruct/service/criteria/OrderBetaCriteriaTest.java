package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderBetaCriteriaTest {

    @Test
    void newOrderBetaCriteriaHasAllFiltersNullTest() {
        var orderBetaCriteria = new OrderBetaCriteria();
        assertThat(orderBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var orderBetaCriteria = new OrderBetaCriteria();

        setAllFilters(orderBetaCriteria);

        assertThat(orderBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderBetaCriteriaCopyCreatesNullFilterTest() {
        var orderBetaCriteria = new OrderBetaCriteria();
        var copy = orderBetaCriteria.copy();

        assertThat(orderBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderBetaCriteria)
        );
    }

    @Test
    void orderBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderBetaCriteria = new OrderBetaCriteria();
        setAllFilters(orderBetaCriteria);

        var copy = orderBetaCriteria.copy();

        assertThat(orderBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderBetaCriteria = new OrderBetaCriteria();

        assertThat(orderBetaCriteria).hasToString("OrderBetaCriteria{}");
    }

    private static void setAllFilters(OrderBetaCriteria orderBetaCriteria) {
        orderBetaCriteria.id();
        orderBetaCriteria.orderDate();
        orderBetaCriteria.totalPrice();
        orderBetaCriteria.status();
        orderBetaCriteria.productsId();
        orderBetaCriteria.paymentId();
        orderBetaCriteria.shipmentId();
        orderBetaCriteria.tenantId();
        orderBetaCriteria.customerId();
        orderBetaCriteria.distinct();
    }

    private static Condition<OrderBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderBetaCriteria> copyFiltersAre(OrderBetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
