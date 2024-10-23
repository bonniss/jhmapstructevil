package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class NextPaymentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentAllPropertiesEquals(NextPayment expected, NextPayment actual) {
        assertNextPaymentAutoGeneratedPropertiesEquals(expected, actual);
        assertNextPaymentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentAllUpdatablePropertiesEquals(NextPayment expected, NextPayment actual) {
        assertNextPaymentUpdatableFieldsEquals(expected, actual);
        assertNextPaymentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentAutoGeneratedPropertiesEquals(NextPayment expected, NextPayment actual) {
        assertThat(expected)
            .as("Verify NextPayment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentUpdatableFieldsEquals(NextPayment expected, NextPayment actual) {
        assertThat(expected)
            .as("Verify NextPayment relevant properties")
            .satisfies(e -> assertThat(e.getAmount()).as("check amount").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getAmount()))
            .satisfies(e -> assertThat(e.getPaymentDate()).as("check paymentDate").isEqualTo(actual.getPaymentDate()))
            .satisfies(e -> assertThat(e.getPaymentMethod()).as("check paymentMethod").isEqualTo(actual.getPaymentMethod()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentUpdatableRelationshipsEquals(NextPayment expected, NextPayment actual) {
        assertThat(expected)
            .as("Verify NextPayment relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
