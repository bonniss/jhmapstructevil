package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NextOrderAlphaCriteriaTest {

    @Test
    void newNextOrderAlphaCriteriaHasAllFiltersNullTest() {
        var nextOrderAlphaCriteria = new NextOrderAlphaCriteria();
        assertThat(nextOrderAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void nextOrderAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var nextOrderAlphaCriteria = new NextOrderAlphaCriteria();

        setAllFilters(nextOrderAlphaCriteria);

        assertThat(nextOrderAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void nextOrderAlphaCriteriaCopyCreatesNullFilterTest() {
        var nextOrderAlphaCriteria = new NextOrderAlphaCriteria();
        var copy = nextOrderAlphaCriteria.copy();

        assertThat(nextOrderAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderAlphaCriteria)
        );
    }

    @Test
    void nextOrderAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var nextOrderAlphaCriteria = new NextOrderAlphaCriteria();
        setAllFilters(nextOrderAlphaCriteria);

        var copy = nextOrderAlphaCriteria.copy();

        assertThat(nextOrderAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(nextOrderAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var nextOrderAlphaCriteria = new NextOrderAlphaCriteria();

        assertThat(nextOrderAlphaCriteria).hasToString("NextOrderAlphaCriteria{}");
    }

    private static void setAllFilters(NextOrderAlphaCriteria nextOrderAlphaCriteria) {
        nextOrderAlphaCriteria.id();
        nextOrderAlphaCriteria.orderDate();
        nextOrderAlphaCriteria.totalPrice();
        nextOrderAlphaCriteria.status();
        nextOrderAlphaCriteria.productsId();
        nextOrderAlphaCriteria.paymentId();
        nextOrderAlphaCriteria.shipmentId();
        nextOrderAlphaCriteria.tenantId();
        nextOrderAlphaCriteria.customerId();
        nextOrderAlphaCriteria.distinct();
    }

    private static Condition<NextOrderAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<NextOrderAlphaCriteria> copyFiltersAre(
        NextOrderAlphaCriteria copy,
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
