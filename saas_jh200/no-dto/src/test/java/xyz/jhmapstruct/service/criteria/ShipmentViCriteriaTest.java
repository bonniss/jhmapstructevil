package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentViCriteriaTest {

    @Test
    void newShipmentViCriteriaHasAllFiltersNullTest() {
        var shipmentViCriteria = new ShipmentViCriteria();
        assertThat(shipmentViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentViCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentViCriteria = new ShipmentViCriteria();

        setAllFilters(shipmentViCriteria);

        assertThat(shipmentViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentViCriteriaCopyCreatesNullFilterTest() {
        var shipmentViCriteria = new ShipmentViCriteria();
        var copy = shipmentViCriteria.copy();

        assertThat(shipmentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentViCriteria)
        );
    }

    @Test
    void shipmentViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentViCriteria = new ShipmentViCriteria();
        setAllFilters(shipmentViCriteria);

        var copy = shipmentViCriteria.copy();

        assertThat(shipmentViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentViCriteria = new ShipmentViCriteria();

        assertThat(shipmentViCriteria).hasToString("ShipmentViCriteria{}");
    }

    private static void setAllFilters(ShipmentViCriteria shipmentViCriteria) {
        shipmentViCriteria.id();
        shipmentViCriteria.trackingNumber();
        shipmentViCriteria.shippedDate();
        shipmentViCriteria.deliveryDate();
        shipmentViCriteria.tenantId();
        shipmentViCriteria.distinct();
    }

    private static Condition<ShipmentViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentViCriteria> copyFiltersAre(ShipmentViCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
