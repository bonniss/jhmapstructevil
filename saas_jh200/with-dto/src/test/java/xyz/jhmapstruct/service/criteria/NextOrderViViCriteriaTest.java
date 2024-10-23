package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderViViCriteriaTest {

    @Test
    void newNextOrderViViCriteriaHasAllFiltersNullTest() {
        var nextOrderViViCriteria = new NextOrderViViCriteria();
        assertThat(nextOrderViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderViViCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderViViCriteria = new NextOrderViViCriteria();

        setAllFilters(nextOrderViViCriteria);

        assertThat(nextOrderViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderViViCriteriaCopyCreatesNullFilterTest() {
        var nextOrderViViCriteria = new NextOrderViViCriteria();
        var copy = nextOrderViViCriteria.copy();

        assertThat(nextOrderViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderViViCriteria)
        );
    }

    @Test
    void nextOrderViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderViViCriteria = new NextOrderViViCriteria();
        setAllFilters(nextOrderViViCriteria);

        var copy = nextOrderViViCriteria.copy();

        assertThat(nextOrderViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderViViCriteria = new NextOrderViViCriteria();

        assertThat(nextOrderViViCriteria).hasToString("NextOrderViViCriteria{}");
    }

    private static void setAllFilters(NextOrderViViCriteria nextOrderViViCriteria) {
        nextOrderViViCriteria.id();
        nextOrderViViCriteria.orderDate();
        nextOrderViViCriteria.totalPrice();
        nextOrderViViCriteria.status();
        nextOrderViViCriteria.productsId();
        nextOrderViViCriteria.paymentId();
        nextOrderViViCriteria.shipmentId();
        nextOrderViViCriteria.tenantId();
        nextOrderViViCriteria.customerId();
        nextOrderViViCriteria.distinct();
    }

    private static Condition<NextOrderViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderViViCriteria> copyFiltersAre(
        NextOrderViViCriteria copy,
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
