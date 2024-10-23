package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class PaymentThetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentThetaAllPropertiesEquals(PaymentTheta expected, PaymentTheta actual) {
        assertPaymentThetaAutoGeneratedPropertiesEquals(expected, actual);
        assertPaymentThetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentThetaAllUpdatablePropertiesEquals(PaymentTheta expected, PaymentTheta actual) {
        assertPaymentThetaUpdatableFieldsEquals(expected, actual);
        assertPaymentThetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentThetaAutoGeneratedPropertiesEquals(PaymentTheta expected, PaymentTheta actual) {
        assertThat(expected)
            .as("Verify PaymentTheta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentThetaUpdatableFieldsEquals(PaymentTheta expected, PaymentTheta actual) {
        assertThat(expected)
            .as("Verify PaymentTheta relevant properties")
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
    public static void assertPaymentThetaUpdatableRelationshipsEquals(PaymentTheta expected, PaymentTheta actual) {
        assertThat(expected)
            .as("Verify PaymentTheta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
