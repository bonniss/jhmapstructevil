package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class PaymentBetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentBetaAllPropertiesEquals(PaymentBeta expected, PaymentBeta actual) {
        assertPaymentBetaAutoGeneratedPropertiesEquals(expected, actual);
        assertPaymentBetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentBetaAllUpdatablePropertiesEquals(PaymentBeta expected, PaymentBeta actual) {
        assertPaymentBetaUpdatableFieldsEquals(expected, actual);
        assertPaymentBetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentBetaAutoGeneratedPropertiesEquals(PaymentBeta expected, PaymentBeta actual) {
        assertThat(expected)
            .as("Verify PaymentBeta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentBetaUpdatableFieldsEquals(PaymentBeta expected, PaymentBeta actual) {
        assertThat(expected)
            .as("Verify PaymentBeta relevant properties")
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
    public static void assertPaymentBetaUpdatableRelationshipsEquals(PaymentBeta expected, PaymentBeta actual) {
        assertThat(expected)
            .as("Verify PaymentBeta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}