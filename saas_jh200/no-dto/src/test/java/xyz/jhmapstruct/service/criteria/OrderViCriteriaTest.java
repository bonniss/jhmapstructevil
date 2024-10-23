package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderViCriteriaTest {

    @Test
    void newOrderViCriteriaHasAllFiltersNullTest() {
        var orderViCriteria = new OrderViCriteria();
        assertThat(orderViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderViCriteriaFluentMethodsCreatesFiltersTest() {
        var orderViCriteria = new OrderViCriteria();

        setAllFilters(orderViCriteria);

        assertThat(orderViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderViCriteriaCopyCreatesNullFilterTest() {
        var orderViCriteria = new OrderViCriteria();
        var copy = orderViCriteria.copy();

        assertThat(orderViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderViCriteria)
        );
    }

    @Test
    void orderViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderViCriteria = new OrderViCriteria();
        setAllFilters(orderViCriteria);

        var copy = orderViCriteria.copy();

        assertThat(orderViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderViCriteria = new OrderViCriteria();

        assertThat(orderViCriteria).hasToString("OrderViCriteria{}");
    }

    private static void setAllFilters(OrderViCriteria orderViCriteria) {
        orderViCriteria.id();
        orderViCriteria.orderDate();
        orderViCriteria.totalPrice();
        orderViCriteria.status();
        orderViCriteria.productsId();
        orderViCriteria.paymentId();
        orderViCriteria.shipmentId();
        orderViCriteria.tenantId();
        orderViCriteria.customerId();
        orderViCriteria.distinct();
    }

    private static Condition<OrderViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderViCriteria> copyFiltersAre(OrderViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
