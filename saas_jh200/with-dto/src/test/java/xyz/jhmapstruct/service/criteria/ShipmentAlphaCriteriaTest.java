package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentAlphaCriteriaTest {

    @Test
    void newShipmentAlphaCriteriaHasAllFiltersNullTest() {
        var shipmentAlphaCriteria = new ShipmentAlphaCriteria();
        assertThat(shipmentAlphaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentAlphaCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentAlphaCriteria = new ShipmentAlphaCriteria();

        setAllFilters(shipmentAlphaCriteria);

        assertThat(shipmentAlphaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentAlphaCriteriaCopyCreatesNullFilterTest() {
        var shipmentAlphaCriteria = new ShipmentAlphaCriteria();
        var copy = shipmentAlphaCriteria.copy();

        assertThat(shipmentAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentAlphaCriteria)
        );
    }

    @Test
    void shipmentAlphaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentAlphaCriteria = new ShipmentAlphaCriteria();
        setAllFilters(shipmentAlphaCriteria);

        var copy = shipmentAlphaCriteria.copy();

        assertThat(shipmentAlphaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentAlphaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentAlphaCriteria = new ShipmentAlphaCriteria();

        assertThat(shipmentAlphaCriteria).hasToString("ShipmentAlphaCriteria{}");
    }

    private static void setAllFilters(ShipmentAlphaCriteria shipmentAlphaCriteria) {
        shipmentAlphaCriteria.id();
        shipmentAlphaCriteria.trackingNumber();
        shipmentAlphaCriteria.shippedDate();
        shipmentAlphaCriteria.deliveryDate();
        shipmentAlphaCriteria.tenantId();
        shipmentAlphaCriteria.distinct();
    }

    private static Condition<ShipmentAlphaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTrackingNumber()) &&
                condition.apply(criteria.getShippedDate()) &&
                condition.apply(criteria.getDeliveryDate()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShipmentAlphaCriteria> copyFiltersAre(
        ShipmentAlphaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTrackingNumber(), copy.getTrackingNumber()) &&
                condition.apply(criteria.getShippedDate(), copy.getShippedDate()) &&
                condition.apply(criteria.getDeliveryDate(), copy.getDeliveryDate()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
