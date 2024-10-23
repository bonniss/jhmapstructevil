package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ShipmentGammaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentGammaAllPropertiesEquals(ShipmentGamma expected, ShipmentGamma actual) {
        assertShipmentGammaAutoGeneratedPropertiesEquals(expected, actual);
        assertShipmentGammaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentGammaAllUpdatablePropertiesEquals(ShipmentGamma expected, ShipmentGamma actual) {
        assertShipmentGammaUpdatableFieldsEquals(expected, actual);
        assertShipmentGammaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentGammaAutoGeneratedPropertiesEquals(ShipmentGamma expected, ShipmentGamma actual) {
        assertThat(expected)
            .as("Verify ShipmentGamma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentGammaUpdatableFieldsEquals(ShipmentGamma expected, ShipmentGamma actual) {
        assertThat(expected)
            .as("Verify ShipmentGamma relevant properties")
            .satisfies(e -> assertThat(e.getTrackingNumber()).as("check trackingNumber").isEqualTo(actual.getTrackingNumber()))
            .satisfies(e -> assertThat(e.getShippedDate()).as("check shippedDate").isEqualTo(actual.getShippedDate()))
            .satisfies(e -> assertThat(e.getDeliveryDate()).as("check deliveryDate").isEqualTo(actual.getDeliveryDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentGammaUpdatableRelationshipsEquals(ShipmentGamma expected, ShipmentGamma actual) {
        assertThat(expected)
            .as("Verify ShipmentGamma relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}