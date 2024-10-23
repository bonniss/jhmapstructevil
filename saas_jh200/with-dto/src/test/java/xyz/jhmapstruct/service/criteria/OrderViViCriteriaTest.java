package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderViViCriteriaTest {

    @Test
    void newOrderViViCriteriaHasAllFiltersNullTest() {
        var orderViViCriteria = new OrderViViCriteria();
        assertThat(orderViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderViViCriteriaFluentMethodsCreatesFiltersTest() {
        var orderViViCriteria = new OrderViViCriteria();

        setAllFilters(orderViViCriteria);

        assertThat(orderViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderViViCriteriaCopyCreatesNullFilterTest() {
        var orderViViCriteria = new OrderViViCriteria();
        var copy = orderViViCriteria.copy();

        assertThat(orderViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderViViCriteria)
        );
    }

    @Test
    void orderViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderViViCriteria = new OrderViViCriteria();
        setAllFilters(orderViViCriteria);

        var copy = orderViViCriteria.copy();

        assertThat(orderViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderViViCriteria = new OrderViViCriteria();

        assertThat(orderViViCriteria).hasToString("OrderViViCriteria{}");
    }

    private static void setAllFilters(OrderViViCriteria orderViViCriteria) {
        orderViViCriteria.id();
        orderViViCriteria.orderDate();
        orderViViCriteria.totalPrice();
        orderViViCriteria.status();
        orderViViCriteria.productsId();
        orderViViCriteria.paymentId();
        orderViViCriteria.shipmentId();
        orderViViCriteria.tenantId();
        orderViViCriteria.customerId();
        orderViViCriteria.distinct();
    }

    private static Condition<OrderViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderViViCriteria> copyFiltersAre(OrderViViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
