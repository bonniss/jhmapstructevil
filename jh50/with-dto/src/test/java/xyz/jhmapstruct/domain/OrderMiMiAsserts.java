package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class OrderMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderMiMiAllPropertiesEquals(OrderMiMi expected, OrderMiMi actual) {
        assertOrderMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertOrderMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderMiMiAllUpdatablePropertiesEquals(OrderMiMi expected, OrderMiMi actual) {
        assertOrderMiMiUpdatableFieldsEquals(expected, actual);
        assertOrderMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderMiMiAutoGeneratedPropertiesEquals(OrderMiMi expected, OrderMiMi actual) {
        assertThat(expected)
            .as("Verify OrderMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderMiMiUpdatableFieldsEquals(OrderMiMi expected, OrderMiMi actual) {
        assertThat(expected)
            .as("Verify OrderMiMi relevant properties")
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
    public static void assertOrderMiMiUpdatableRelationshipsEquals(OrderMiMi expected, OrderMiMi actual) {
        assertThat(expected)
            .as("Verify OrderMiMi relationships")
            .satisfies(e -> assertThat(e.getPayment()).as("check payment").isEqualTo(actual.getPayment()))
            .satisfies(e -> assertThat(e.getShipment()).as("check shipment").isEqualTo(actual.getShipment()))
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()));
    }
}
