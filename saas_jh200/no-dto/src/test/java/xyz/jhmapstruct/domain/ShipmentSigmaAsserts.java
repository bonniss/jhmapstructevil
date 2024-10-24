package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ShipmentSigmaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentSigmaAllPropertiesEquals(ShipmentSigma expected, ShipmentSigma actual) {
        assertShipmentSigmaAutoGeneratedPropertiesEquals(expected, actual);
        assertShipmentSigmaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentSigmaAllUpdatablePropertiesEquals(ShipmentSigma expected, ShipmentSigma actual) {
        assertShipmentSigmaUpdatableFieldsEquals(expected, actual);
        assertShipmentSigmaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentSigmaAutoGeneratedPropertiesEquals(ShipmentSigma expected, ShipmentSigma actual) {
        assertThat(expected)
            .as("Verify ShipmentSigma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertShipmentSigmaUpdatableFieldsEquals(ShipmentSigma expected, ShipmentSigma actual) {
        assertThat(expected)
            .as("Verify ShipmentSigma relevant properties")
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
    public static void assertShipmentSigmaUpdatableRelationshipsEquals(ShipmentSigma expected, ShipmentSigma actual) {
        assertThat(expected)
            .as("Verify ShipmentSigma relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
