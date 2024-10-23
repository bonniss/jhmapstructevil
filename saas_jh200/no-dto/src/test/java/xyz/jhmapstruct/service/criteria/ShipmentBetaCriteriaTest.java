package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentBetaCriteriaTest {

    @Test
    void newShipmentBetaCriteriaHasAllFiltersNullTest() {
        var shipmentBetaCriteria = new ShipmentBetaCriteria();
        assertThat(shipmentBetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentBetaCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentBetaCriteria = new ShipmentBetaCriteria();

        setAllFilters(shipmentBetaCriteria);

        assertThat(shipmentBetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentBetaCriteriaCopyCreatesNullFilterTest() {
        var shipmentBetaCriteria = new ShipmentBetaCriteria();
        var copy = shipmentBetaCriteria.copy();

        assertThat(shipmentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentBetaCriteria)
        );
    }

    @Test
    void shipmentBetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentBetaCriteria = new ShipmentBetaCriteria();
        setAllFilters(shipmentBetaCriteria);

        var copy = shipmentBetaCriteria.copy();

        assertThat(shipmentBetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentBetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentBetaCriteria = new ShipmentBetaCriteria();

        assertThat(shipmentBetaCriteria).hasToString("ShipmentBetaCriteria{}");
    }

    private static void setAllFilters(ShipmentBetaCriteria shipmentBetaCriteria) {
        shipmentBetaCriteria.id();
        shipmentBetaCriteria.trackingNumber();
        shipmentBetaCriteria.shippedDate();
        shipmentBetaCriteria.deliveryDate();
        shipmentBetaCriteria.tenantId();
        shipmentBetaCriteria.distinct();
    }

    private static Condition<ShipmentBetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentBetaCriteria> copyFiltersAre(
        ShipmentBetaCriteria copy,
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
