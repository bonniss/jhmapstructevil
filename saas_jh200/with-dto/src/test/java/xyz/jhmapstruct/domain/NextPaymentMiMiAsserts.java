package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class NextPaymentMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentMiMiAllPropertiesEquals(NextPaymentMiMi expected, NextPaymentMiMi actual) {
        assertNextPaymentMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertNextPaymentMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentMiMiAllUpdatablePropertiesEquals(NextPaymentMiMi expected, NextPaymentMiMi actual) {
        assertNextPaymentMiMiUpdatableFieldsEquals(expected, actual);
        assertNextPaymentMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentMiMiAutoGeneratedPropertiesEquals(NextPaymentMiMi expected, NextPaymentMiMi actual) {
        assertThat(expected)
            .as("Verify NextPaymentMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentMiMiUpdatableFieldsEquals(NextPaymentMiMi expected, NextPaymentMiMi actual) {
        assertThat(expected)
            .as("Verify NextPaymentMiMi relevant properties")
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
    public static void assertNextPaymentMiMiUpdatableRelationshipsEquals(NextPaymentMiMi expected, NextPaymentMiMi actual) {
        assertThat(expected)
            .as("Verify NextPaymentMiMi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
