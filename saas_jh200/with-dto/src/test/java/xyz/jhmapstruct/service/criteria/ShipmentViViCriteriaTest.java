package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentViViCriteriaTest {

    @Test
    void newShipmentViViCriteriaHasAllFiltersNullTest() {
        var shipmentViViCriteria = new ShipmentViViCriteria();
        assertThat(shipmentViViCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentViViCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentViViCriteria = new ShipmentViViCriteria();

        setAllFilters(shipmentViViCriteria);

        assertThat(shipmentViViCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentViViCriteriaCopyCreatesNullFilterTest() {
        var shipmentViViCriteria = new ShipmentViViCriteria();
        var copy = shipmentViViCriteria.copy();

        assertThat(shipmentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentViViCriteria)
        );
    }

    @Test
    void shipmentViViCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentViViCriteria = new ShipmentViViCriteria();
        setAllFilters(shipmentViViCriteria);

        var copy = shipmentViViCriteria.copy();

        assertThat(shipmentViViCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentViViCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentViViCriteria = new ShipmentViViCriteria();

        assertThat(shipmentViViCriteria).hasToString("ShipmentViViCriteria{}");
    }

    private static void setAllFilters(ShipmentViViCriteria shipmentViViCriteria) {
        shipmentViViCriteria.id();
        shipmentViViCriteria.trackingNumber();
        shipmentViViCriteria.shippedDate();
        shipmentViViCriteria.deliveryDate();
        shipmentViViCriteria.tenantId();
        shipmentViViCriteria.distinct();
    }

    private static Condition<ShipmentViViCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentViViCriteria> copyFiltersAre(
        ShipmentViViCriteria copy,
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
