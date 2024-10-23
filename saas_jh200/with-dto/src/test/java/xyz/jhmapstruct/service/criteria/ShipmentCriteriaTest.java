package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentCriteriaTest {

    @Test
    void newShipmentCriteriaHasAllFiltersNullTest() {
        var shipmentCriteria = new ShipmentCriteria();
        assertThat(shipmentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentCriteria = new ShipmentCriteria();

        setAllFilters(shipmentCriteria);

        assertThat(shipmentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentCriteriaCopyCreatesNullFilterTest() {
        var shipmentCriteria = new ShipmentCriteria();
        var copy = shipmentCriteria.copy();

        assertThat(shipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentCriteria)
        );
    }

    @Test
    void shipmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentCriteria = new ShipmentCriteria();
        setAllFilters(shipmentCriteria);

        var copy = shipmentCriteria.copy();

        assertThat(shipmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentCriteria = new ShipmentCriteria();

        assertThat(shipmentCriteria).hasToString("ShipmentCriteria{}");
    }

    private static void setAllFilters(ShipmentCriteria shipmentCriteria) {
        shipmentCriteria.id();
        shipmentCriteria.trackingNumber();
        shipmentCriteria.shippedDate();
        shipmentCriteria.deliveryDate();
        shipmentCriteria.tenantId();
        shipmentCriteria.distinct();
    }

    private static Condition<ShipmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentCriteria> copyFiltersAre(ShipmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
