package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class NextPaymentBetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentBetaAllPropertiesEquals(NextPaymentBeta expected, NextPaymentBeta actual) {
        assertNextPaymentBetaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextPaymentBetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentBetaAllUpdatablePropertiesEquals(NextPaymentBeta expected, NextPaymentBeta actual) {
        assertNextPaymentBetaUpdatableFieldsEquals(expected, actual);
        assertNextPaymentBetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentBetaAutoGeneratedPropertiesEquals(NextPaymentBeta expected, NextPaymentBeta actual) {
        assertThat(expected)
            .as("Verify NextPaymentBeta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextPaymentBetaUpdatableFieldsEquals(NextPaymentBeta expected, NextPaymentBeta actual) {
        assertThat(expected)
            .as("Verify NextPaymentBeta relevant properties")
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
    public static void assertNextPaymentBetaUpdatableRelationshipsEquals(NextPaymentBeta expected, NextPaymentBeta actual) {
        assertThat(expected)
            .as("Verify NextPaymentBeta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
