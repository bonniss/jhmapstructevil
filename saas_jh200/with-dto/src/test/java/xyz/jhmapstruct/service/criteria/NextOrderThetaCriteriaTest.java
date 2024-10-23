package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderThetaCriteriaTest {

    @Test
    void newNextOrderThetaCriteriaHasAllFiltersNullTest() {
        var nextOrderThetaCriteria = new NextOrderThetaCriteria();
        assertThat(nextOrderThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderThetaCriteria = new NextOrderThetaCriteria();

        setAllFilters(nextOrderThetaCriteria);

        assertThat(nextOrderThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderThetaCriteriaCopyCreatesNullFilterTest() {
        var nextOrderThetaCriteria = new NextOrderThetaCriteria();
        var copy = nextOrderThetaCriteria.copy();

        assertThat(nextOrderThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderThetaCriteria)
        );
    }

    @Test
    void nextOrderThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderThetaCriteria = new NextOrderThetaCriteria();
        setAllFilters(nextOrderThetaCriteria);

        var copy = nextOrderThetaCriteria.copy();

        assertThat(nextOrderThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderThetaCriteria = new NextOrderThetaCriteria();

        assertThat(nextOrderThetaCriteria).hasToString("NextOrderThetaCriteria{}");
    }

    private static void setAllFilters(NextOrderThetaCriteria nextOrderThetaCriteria) {
        nextOrderThetaCriteria.id();
        nextOrderThetaCriteria.orderDate();
        nextOrderThetaCriteria.totalPrice();
        nextOrderThetaCriteria.status();
        nextOrderThetaCriteria.productsId();
        nextOrderThetaCriteria.paymentId();
        nextOrderThetaCriteria.shipmentId();
        nextOrderThetaCriteria.tenantId();
        nextOrderThetaCriteria.customerId();
        nextOrderThetaCriteria.distinct();
    }

    private static Condition<NextOrderThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderThetaCriteria> copyFiltersAre(
        NextOrderThetaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
