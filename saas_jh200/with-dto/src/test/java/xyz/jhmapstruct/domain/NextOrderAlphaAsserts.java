package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class NextOrderAlphaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextOrderAlphaAllPropertiesEquals(NextOrderAlpha expected, NextOrderAlpha actual) {
        assertNextOrderAlphaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextOrderAlphaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextOrderAlphaAllUpdatablePropertiesEquals(NextOrderAlpha expected, NextOrderAlpha actual) {
        assertNextOrderAlphaUpdatableFieldsEquals(expected, actual);
        assertNextOrderAlphaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextOrderAlphaAutoGeneratedPropertiesEquals(NextOrderAlpha expected, NextOrderAlpha actual) {
        assertThat(expected)
            .as("Verify NextOrderAlpha auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextOrderAlphaUpdatableFieldsEquals(NextOrderAlpha expected, NextOrderAlpha actual) {
        assertThat(expected)
            .as("Verify NextOrderAlpha relevant properties")
            .satisfies(e -> assertThat(e.getOrderDate()).as("check orderDate").isEqualTo(actual.getOrderDate()))
            .satisfies(e ->
                assertThat(e.getTotalPrice()).as("check totalPrice").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getTotalPrice())
            )
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextOrderAlphaUpdatableRelationshipsEquals(NextOrderAlpha expected, NextOrderAlpha actual) {
        assertThat(expected)
            .as("Verify NextOrderAlpha relationships")
            .satisfies(e -> assertThat(e.getPayment()).as("check payment").isEqualTo(actual.getPayment()))
            .satisfies(e -> assertThat(e.getShipment()).as("check shipment").isEqualTo(actual.getShipment()))
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()))
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()));
    }
}
