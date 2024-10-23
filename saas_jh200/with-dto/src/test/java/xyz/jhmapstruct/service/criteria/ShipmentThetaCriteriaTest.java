package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentThetaCriteriaTest {

    @Test
    void newShipmentThetaCriteriaHasAllFiltersNullTest() {
        var shipmentThetaCriteria = new ShipmentThetaCriteria();
        assertThat(shipmentThetaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentThetaCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentThetaCriteria = new ShipmentThetaCriteria();

        setAllFilters(shipmentThetaCriteria);

        assertThat(shipmentThetaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentThetaCriteriaCopyCreatesNullFilterTest() {
        var shipmentThetaCriteria = new ShipmentThetaCriteria();
        var copy = shipmentThetaCriteria.copy();

        assertThat(shipmentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentThetaCriteria)
        );
    }

    @Test
    void shipmentThetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentThetaCriteria = new ShipmentThetaCriteria();
        setAllFilters(shipmentThetaCriteria);

        var copy = shipmentThetaCriteria.copy();

        assertThat(shipmentThetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentThetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentThetaCriteria = new ShipmentThetaCriteria();

        assertThat(shipmentThetaCriteria).hasToString("ShipmentThetaCriteria{}");
    }

    private static void setAllFilters(ShipmentThetaCriteria shipmentThetaCriteria) {
        shipmentThetaCriteria.id();
        shipmentThetaCriteria.trackingNumber();
        shipmentThetaCriteria.shippedDate();
        shipmentThetaCriteria.deliveryDate();
        shipmentThetaCriteria.tenantId();
        shipmentThetaCriteria.distinct();
    }

    private static Condition<ShipmentThetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentThetaCriteria> copyFiltersAre(
        ShipmentThetaCriteria copy,
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
