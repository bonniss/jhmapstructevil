package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderMiMiCriteriaTest {

    @Test
    void newNextOrderMiMiCriteriaHasAllFiltersNullTest() {
        var nextOrderMiMiCriteria = new NextOrderMiMiCriteria();
        assertThat(nextOrderMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderMiMiCriteria = new NextOrderMiMiCriteria();

        setAllFilters(nextOrderMiMiCriteria);

        assertThat(nextOrderMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderMiMiCriteriaCopyCreatesNullFilterTest() {
        var nextOrderMiMiCriteria = new NextOrderMiMiCriteria();
        var copy = nextOrderMiMiCriteria.copy();

        assertThat(nextOrderMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderMiMiCriteria)
        );
    }

    @Test
    void nextOrderMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderMiMiCriteria = new NextOrderMiMiCriteria();
        setAllFilters(nextOrderMiMiCriteria);

        var copy = nextOrderMiMiCriteria.copy();

        assertThat(nextOrderMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderMiMiCriteria = new NextOrderMiMiCriteria();

        assertThat(nextOrderMiMiCriteria).hasToString("NextOrderMiMiCriteria{}");
    }

    private static void setAllFilters(NextOrderMiMiCriteria nextOrderMiMiCriteria) {
        nextOrderMiMiCriteria.id();
        nextOrderMiMiCriteria.orderDate();
        nextOrderMiMiCriteria.totalPrice();
        nextOrderMiMiCriteria.status();
        nextOrderMiMiCriteria.productsId();
        nextOrderMiMiCriteria.paymentId();
        nextOrderMiMiCriteria.shipmentId();
        nextOrderMiMiCriteria.tenantId();
        nextOrderMiMiCriteria.customerId();
        nextOrderMiMiCriteria.distinct();
    }

    private static Condition<NextOrderMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderMiMiCriteria> copyFiltersAre(
        NextOrderMiMiCriteria copy,
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
