package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentSigmaCriteriaTest {

    @Test
    void newShipmentSigmaCriteriaHasAllFiltersNullTest() {
        var shipmentSigmaCriteria = new ShipmentSigmaCriteria();
        assertThat(shipmentSigmaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentSigmaCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentSigmaCriteria = new ShipmentSigmaCriteria();

        setAllFilters(shipmentSigmaCriteria);

        assertThat(shipmentSigmaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentSigmaCriteriaCopyCreatesNullFilterTest() {
        var shipmentSigmaCriteria = new ShipmentSigmaCriteria();
        var copy = shipmentSigmaCriteria.copy();

        assertThat(shipmentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentSigmaCriteria)
        );
    }

    @Test
    void shipmentSigmaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentSigmaCriteria = new ShipmentSigmaCriteria();
        setAllFilters(shipmentSigmaCriteria);

        var copy = shipmentSigmaCriteria.copy();

        assertThat(shipmentSigmaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentSigmaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentSigmaCriteria = new ShipmentSigmaCriteria();

        assertThat(shipmentSigmaCriteria).hasToString("ShipmentSigmaCriteria{}");
    }

    private static void setAllFilters(ShipmentSigmaCriteria shipmentSigmaCriteria) {
        shipmentSigmaCriteria.id();
        shipmentSigmaCriteria.trackingNumber();
        shipmentSigmaCriteria.shippedDate();
        shipmentSigmaCriteria.deliveryDate();
        shipmentSigmaCriteria.tenantId();
        shipmentSigmaCriteria.distinct();
    }

    private static Condition<ShipmentSigmaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentSigmaCriteria> copyFiltersAre(
        ShipmentSigmaCriteria copy,
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
