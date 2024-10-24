package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class PaymentSigmaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentSigmaAllPropertiesEquals(PaymentSigma expected, PaymentSigma actual) {
        assertPaymentSigmaAutoGeneratedPropertiesEquals(expected, actual);
        assertPaymentSigmaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentSigmaAllUpdatablePropertiesEquals(PaymentSigma expected, PaymentSigma actual) {
        assertPaymentSigmaUpdatableFieldsEquals(expected, actual);
        assertPaymentSigmaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentSigmaAutoGeneratedPropertiesEquals(PaymentSigma expected, PaymentSigma actual) {
        assertThat(expected)
            .as("Verify PaymentSigma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentSigmaUpdatableFieldsEquals(PaymentSigma expected, PaymentSigma actual) {
        assertThat(expected)
            .as("Verify PaymentSigma relevant properties")
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
    public static void assertPaymentSigmaUpdatableRelationshipsEquals(PaymentSigma expected, PaymentSigma actual) {
        assertThat(expected)
            .as("Verify PaymentSigma relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
