package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentMiMiCriteriaTest {

    @Test
    void newShipmentMiMiCriteriaHasAllFiltersNullTest() {
        var shipmentMiMiCriteria = new ShipmentMiMiCriteria();
        assertThat(shipmentMiMiCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentMiMiCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentMiMiCriteria = new ShipmentMiMiCriteria();

        setAllFilters(shipmentMiMiCriteria);

        assertThat(shipmentMiMiCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentMiMiCriteriaCopyCreatesNullFilterTest() {
        var shipmentMiMiCriteria = new ShipmentMiMiCriteria();
        var copy = shipmentMiMiCriteria.copy();

        assertThat(shipmentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentMiMiCriteria)
        );
    }

    @Test
    void shipmentMiMiCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentMiMiCriteria = new ShipmentMiMiCriteria();
        setAllFilters(shipmentMiMiCriteria);

        var copy = shipmentMiMiCriteria.copy();

        assertThat(shipmentMiMiCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentMiMiCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentMiMiCriteria = new ShipmentMiMiCriteria();

        assertThat(shipmentMiMiCriteria).hasToString("ShipmentMiMiCriteria{}");
    }

    private static void setAllFilters(ShipmentMiMiCriteria shipmentMiMiCriteria) {
        shipmentMiMiCriteria.id();
        shipmentMiMiCriteria.trackingNumber();
        shipmentMiMiCriteria.shippedDate();
        shipmentMiMiCriteria.deliveryDate();
        shipmentMiMiCriteria.tenantId();
        shipmentMiMiCriteria.distinct();
    }

    private static Condition<ShipmentMiMiCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentMiMiCriteria> copyFiltersAre(
        ShipmentMiMiCriteria copy,
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
