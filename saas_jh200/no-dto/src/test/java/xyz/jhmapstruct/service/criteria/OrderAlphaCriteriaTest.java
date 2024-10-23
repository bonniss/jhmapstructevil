package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrderAlphaCriteriaTest {

    @Test
    void newOrderAlphaCriteriaHasAllFiltersNullTest() {
        var orderAlphaCriteria = new OrderAlphaCriteria();
        assertThat(orderAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void orderAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var orderAlphaCriteria = new OrderAlphaCriteria();

        setAllFilters(orderAlphaCriteria);

        assertThat(orderAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void orderAlphaCriteriaCopyCreatesNullFilterTest() {
        var orderAlphaCriteria = new OrderAlphaCriteria();
        var copy = orderAlphaCriteria.copy();

        assertThat(orderAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(orderAlphaCriteria)
        );
    }

    @Test
    void orderAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var orderAlphaCriteria = new OrderAlphaCriteria();
        setAllFilters(orderAlphaCriteria);

        var copy = orderAlphaCriteria.copy();

        assertThat(orderAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(orderAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var orderAlphaCriteria = new OrderAlphaCriteria();

        assertThat(orderAlphaCriteria).hasToString("OrderAlphaCriteria{}");
    }

    private static void setAllFilters(OrderAlphaCriteria orderAlphaCriteria) {
        orderAlphaCriteria.id();
        orderAlphaCriteria.orderDate();
        orderAlphaCriteria.totalPrice();
        orderAlphaCriteria.status();
        orderAlphaCriteria.productsId();
        orderAlphaCriteria.paymentId();
        orderAlphaCriteria.shipmentId();
        orderAlphaCriteria.tenantId();
        orderAlphaCriteria.customerId();
        orderAlphaCriteria.distinct();
    }

    private static Condition<OrderAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<OrderAlphaCriteria> copyFiltersAre(OrderAlphaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
