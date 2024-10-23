package xyz.jhmapstruct.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShipmentGammaCriteriaTest {

    @Test
    void newShipmentGammaCriteriaHasAllFiltersNullTest() {
        var shipmentGammaCriteria = new ShipmentGammaCriteria();
        assertThat(shipmentGammaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void shipmentGammaCriteriaFluentMethodsCreatesFiltersTest() {
        var shipmentGammaCriteria = new ShipmentGammaCriteria();

        setAllFilters(shipmentGammaCriteria);

        assertThat(shipmentGammaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void shipmentGammaCriteriaCopyCreatesNullFilterTest() {
        var shipmentGammaCriteria = new ShipmentGammaCriteria();
        var copy = shipmentGammaCriteria.copy();

        assertThat(shipmentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentGammaCriteria)
        );
    }

    @Test
    void shipmentGammaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var shipmentGammaCriteria = new ShipmentGammaCriteria();
        setAllFilters(shipmentGammaCriteria);

        var copy = shipmentGammaCriteria.copy();

        assertThat(shipmentGammaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(shipmentGammaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var shipmentGammaCriteria = new ShipmentGammaCriteria();

        assertThat(shipmentGammaCriteria).hasToString("ShipmentGammaCriteria{}");
    }

    private static void setAllFilters(ShipmentGammaCriteria shipmentGammaCriteria) {
        shipmentGammaCriteria.id();
        shipmentGammaCriteria.trackingNumber();
        shipmentGammaCriteria.shippedDate();
        shipmentGammaCriteria.deliveryDate();
        shipmentGammaCriteria.tenantId();
        shipmentGammaCriteria.distinct();
    }

    private static Condition<ShipmentGammaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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

    private static Condition<ShipmentGammaCriteria> copyFiltersAre(
        ShipmentGammaCriteria copy,
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
