package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextShipmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentAllPropertiesEquals(NextShipment expected, NextShipment actual) {
        assertNextShipmentAutoGeneratedPropertiesEquals(expected, actual);
        assertNextShipmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentAllUpdatablePropertiesEquals(NextShipment expected, NextShipment actual) {
        assertNextShipmentUpdatableFieldsEquals(expected, actual);
        assertNextShipmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentAutoGeneratedPropertiesEquals(NextShipment expected, NextShipment actual) {
        assertThat(expected)
            .as("Verify NextShipment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentUpdatableFieldsEquals(NextShipment expected, NextShipment actual) {
        assertThat(expected)
            .as("Verify NextShipment relevant properties")
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
    public static void assertNextShipmentUpdatableRelationshipsEquals(NextShipment expected, NextShipment actual) {
        assertThat(expected)
            .as("Verify NextShipment relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}