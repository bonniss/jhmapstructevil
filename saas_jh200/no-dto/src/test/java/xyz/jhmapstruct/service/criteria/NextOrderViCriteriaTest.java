package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderViCriteriaTest {

    @Test
    void newNextOrderViCriteriaHasAllFiltersNullTest() {
        var nextOrderViCriteria = new NextOrderViCriteria();
        assertThat(nextOrderViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderViCriteria = new NextOrderViCriteria();

        setAllFilters(nextOrderViCriteria);

        assertThat(nextOrderViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderViCriteriaCopyCreatesNullFilterTest() {
        var nextOrderViCriteria = new NextOrderViCriteria();
        var copy = nextOrderViCriteria.copy();

        assertThat(nextOrderViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderViCriteria)
        );
    }

    @Test
    void nextOrderViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderViCriteria = new NextOrderViCriteria();
        setAllFilters(nextOrderViCriteria);

        var copy = nextOrderViCriteria.copy();

        assertThat(nextOrderViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderViCriteria = new NextOrderViCriteria();

        assertThat(nextOrderViCriteria).hasToString("NextOrderViCriteria{}");
    }

    private static void setAllFilters(NextOrderViCriteria nextOrderViCriteria) {
        nextOrderViCriteria.id();
        nextOrderViCriteria.orderDate();
        nextOrderViCriteria.totalPrice();
        nextOrderViCriteria.status();
        nextOrderViCriteria.productsId();
        nextOrderViCriteria.paymentId();
        nextOrderViCriteria.shipmentId();
        nextOrderViCriteria.tenantId();
        nextOrderViCriteria.customerId();
        nextOrderViCriteria.distinct();
    }

    private static Condition<NextOrderViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderViCriteria> copyFiltersAre(NextOrderViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
