package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderCriteriaTest {

    @Test
    void newNextOrderCriteriaHasAllFiltersNullTest() {
        var nextOrderCriteria = new NextOrderCriteria();
        assertThat(nextOrderCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderCriteria = new NextOrderCriteria();

        setAllFilters(nextOrderCriteria);

        assertThat(nextOrderCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderCriteriaCopyCreatesNullFilterTest() {
        var nextOrderCriteria = new NextOrderCriteria();
        var copy = nextOrderCriteria.copy();

        assertThat(nextOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderCriteria)
        );
    }

    @Test
    void nextOrderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderCriteria = new NextOrderCriteria();
        setAllFilters(nextOrderCriteria);

        var copy = nextOrderCriteria.copy();

        assertThat(nextOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderCriteria = new NextOrderCriteria();

        assertThat(nextOrderCriteria).hasToString("NextOrderCriteria{}");
    }

    private static void setAllFilters(NextOrderCriteria nextOrderCriteria) {
        nextOrderCriteria.id();
        nextOrderCriteria.orderDate();
        nextOrderCriteria.totalPrice();
        nextOrderCriteria.status();
        nextOrderCriteria.productsId();
        nextOrderCriteria.paymentId();
        nextOrderCriteria.shipmentId();
        nextOrderCriteria.tenantId();
        nextOrderCriteria.customerId();
        nextOrderCriteria.distinct();
    }

    private static Condition<NextOrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderCriteria> copyFiltersAre(NextOrderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
