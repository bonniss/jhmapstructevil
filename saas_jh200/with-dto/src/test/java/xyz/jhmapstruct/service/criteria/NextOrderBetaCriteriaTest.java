package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderBetaCriteriaTest {

    @Test
    void newNextOrderBetaCriteriaHasAllFiltersNullTest() {
        var nextOrderBetaCriteria = new NextOrderBetaCriteria();
        assertThat(nextOrderBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderBetaCriteria = new NextOrderBetaCriteria();

        setAllFilters(nextOrderBetaCriteria);

        assertThat(nextOrderBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderBetaCriteriaCopyCreatesNullFilterTest() {
        var nextOrderBetaCriteria = new NextOrderBetaCriteria();
        var copy = nextOrderBetaCriteria.copy();

        assertThat(nextOrderBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderBetaCriteria)
        );
    }

    @Test
    void nextOrderBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderBetaCriteria = new NextOrderBetaCriteria();
        setAllFilters(nextOrderBetaCriteria);

        var copy = nextOrderBetaCriteria.copy();

        assertThat(nextOrderBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderBetaCriteria = new NextOrderBetaCriteria();

        assertThat(nextOrderBetaCriteria).hasToString("NextOrderBetaCriteria{}");
    }

    private static void setAllFilters(NextOrderBetaCriteria nextOrderBetaCriteria) {
        nextOrderBetaCriteria.id();
        nextOrderBetaCriteria.orderDate();
        nextOrderBetaCriteria.totalPrice();
        nextOrderBetaCriteria.status();
        nextOrderBetaCriteria.productsId();
        nextOrderBetaCriteria.paymentId();
        nextOrderBetaCriteria.shipmentId();
        nextOrderBetaCriteria.tenantId();
        nextOrderBetaCriteria.customerId();
        nextOrderBetaCriteria.distinct();
    }

    private static Condition<NextOrderBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderBetaCriteria> copyFiltersAre(
        NextOrderBetaCriteria copy,
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
