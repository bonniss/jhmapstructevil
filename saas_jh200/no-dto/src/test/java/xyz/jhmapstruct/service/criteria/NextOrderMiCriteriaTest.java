package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderMiCriteriaTest {

    @Test
    void newNextOrderMiCriteriaHasAllFiltersNullTest() {
        var nextOrderMiCriteria = new NextOrderMiCriteria();
        assertThat(nextOrderMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderMiCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderMiCriteria = new NextOrderMiCriteria();

        setAllFilters(nextOrderMiCriteria);

        assertThat(nextOrderMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderMiCriteriaCopyCreatesNullFilterTest() {
        var nextOrderMiCriteria = new NextOrderMiCriteria();
        var copy = nextOrderMiCriteria.copy();

        assertThat(nextOrderMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderMiCriteria)
        );
    }

    @Test
    void nextOrderMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderMiCriteria = new NextOrderMiCriteria();
        setAllFilters(nextOrderMiCriteria);

        var copy = nextOrderMiCriteria.copy();

        assertThat(nextOrderMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderMiCriteria = new NextOrderMiCriteria();

        assertThat(nextOrderMiCriteria).hasToString("NextOrderMiCriteria{}");
    }

    private static void setAllFilters(NextOrderMiCriteria nextOrderMiCriteria) {
        nextOrderMiCriteria.id();
        nextOrderMiCriteria.orderDate();
        nextOrderMiCriteria.totalPrice();
        nextOrderMiCriteria.status();
        nextOrderMiCriteria.productsId();
        nextOrderMiCriteria.paymentId();
        nextOrderMiCriteria.shipmentId();
        nextOrderMiCriteria.tenantId();
        nextOrderMiCriteria.customerId();
        nextOrderMiCriteria.distinct();
    }

    private static Condition<NextOrderMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderMiCriteria> copyFiltersAre(NextOrderMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
