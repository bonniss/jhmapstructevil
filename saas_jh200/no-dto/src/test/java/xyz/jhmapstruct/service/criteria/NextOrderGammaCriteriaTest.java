package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderGammaCriteriaTest {

    @Test
    void newNextOrderGammaCriteriaHasAllFiltersNullTest() {
        var nextOrderGammaCriteria = new NextOrderGammaCriteria();
        assertThat(nextOrderGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderGammaCriteria = new NextOrderGammaCriteria();

        setAllFilters(nextOrderGammaCriteria);

        assertThat(nextOrderGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderGammaCriteriaCopyCreatesNullFilterTest() {
        var nextOrderGammaCriteria = new NextOrderGammaCriteria();
        var copy = nextOrderGammaCriteria.copy();

        assertThat(nextOrderGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderGammaCriteria)
        );
    }

    @Test
    void nextOrderGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderGammaCriteria = new NextOrderGammaCriteria();
        setAllFilters(nextOrderGammaCriteria);

        var copy = nextOrderGammaCriteria.copy();

        assertThat(nextOrderGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderGammaCriteria = new NextOrderGammaCriteria();

        assertThat(nextOrderGammaCriteria).hasToString("NextOrderGammaCriteria{}");
    }

    private static void setAllFilters(NextOrderGammaCriteria nextOrderGammaCriteria) {
        nextOrderGammaCriteria.id();
        nextOrderGammaCriteria.orderDate();
        nextOrderGammaCriteria.totalPrice();
        nextOrderGammaCriteria.status();
        nextOrderGammaCriteria.productsId();
        nextOrderGammaCriteria.paymentId();
        nextOrderGammaCriteria.shipmentId();
        nextOrderGammaCriteria.tenantId();
        nextOrderGammaCriteria.customerId();
        nextOrderGammaCriteria.distinct();
    }

    private static Condition<NextOrderGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderGammaCriteria> copyFiltersAre(
        NextOrderGammaCriteria copy,
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
