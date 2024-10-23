package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentMiCriteriaTest {

    @Test
    void newShipmentMiCriteriaHasAllFiltersNullTest() {
        var shipmentMiCriteria = new ShipmentMiCriteria();
        assertThat(shipmentMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentMiCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentMiCriteria = new ShipmentMiCriteria();

        setAllFilters(shipmentMiCriteria);

        assertThat(shipmentMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentMiCriteriaCopyCreatesNullFilterTest() {
        var shipmentMiCriteria = new ShipmentMiCriteria();
        var copy = shipmentMiCriteria.copy();

        assertThat(shipmentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentMiCriteria)
        );
    }

    @Test
    void shipmentMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentMiCriteria = new ShipmentMiCriteria();
        setAllFilters(shipmentMiCriteria);

        var copy = shipmentMiCriteria.copy();

        assertThat(shipmentMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentMiCriteria = new ShipmentMiCriteria();

        assertThat(shipmentMiCriteria).hasToString("ShipmentMiCriteria{}");
    }

    private static void setAllFilters(ShipmentMiCriteria shipmentMiCriteria) {
        shipmentMiCriteria.id();
        shipmentMiCriteria.trackingNumber();
        shipmentMiCriteria.shippedDate();
        shipmentMiCriteria.deliveryDate();
        shipmentMiCriteria.tenantId();
        shipmentMiCriteria.distinct();
    }

    private static Condition<ShipmentMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentMiCriteria> copyFiltersAre(ShipmentMiCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
