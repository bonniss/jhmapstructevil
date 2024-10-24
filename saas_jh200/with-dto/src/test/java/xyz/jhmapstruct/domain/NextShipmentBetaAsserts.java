package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextShipmentBetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentBetaAllPropertiesEquals(NextShipmentBeta expected, NextShipmentBeta actual) {
        assertNextShipmentBetaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextShipmentBetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentBetaAllUpdatablePropertiesEquals(NextShipmentBeta expected, NextShipmentBeta actual) {
        assertNextShipmentBetaUpdatableFieldsEquals(expected, actual);
        assertNextShipmentBetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentBetaAutoGeneratedPropertiesEquals(NextShipmentBeta expected, NextShipmentBeta actual) {
        assertThat(expected)
            .as("Verify NextShipmentBeta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextShipmentBetaUpdatableFieldsEquals(NextShipmentBeta expected, NextShipmentBeta actual) {
        assertThat(expected)
            .as("Verify NextShipmentBeta relevant properties")
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
    public static void assertNextShipmentBetaUpdatableRelationshipsEquals(NextShipmentBeta expected, NextShipmentBeta actual) {
        assertThat(expected)
            .as("Verify NextShipmentBeta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
