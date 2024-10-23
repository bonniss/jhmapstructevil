package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class InvoiceThetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceThetaAllPropertiesEquals(InvoiceTheta expected, InvoiceTheta actual) {
        assertInvoiceThetaAutoGeneratedPropertiesEquals(expected, actual);
        assertInvoiceThetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceThetaAllUpdatablePropertiesEquals(InvoiceTheta expected, InvoiceTheta actual) {
        assertInvoiceThetaUpdatableFieldsEquals(expected, actual);
        assertInvoiceThetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceThetaAutoGeneratedPropertiesEquals(InvoiceTheta expected, InvoiceTheta actual) {
        assertThat(expected)
            .as("Verify InvoiceTheta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceThetaUpdatableFieldsEquals(InvoiceTheta expected, InvoiceTheta actual) {
        assertThat(expected)
            .as("Verify InvoiceTheta relevant properties")
            .satisfies(e -> assertThat(e.getInvoiceNumber()).as("check invoiceNumber").isEqualTo(actual.getInvoiceNumber()))
            .satisfies(e -> assertThat(e.getIssueDate()).as("check issueDate").isEqualTo(actual.getIssueDate()))
            .satisfies(e -> assertThat(e.getDueDate()).as("check dueDate").isEqualTo(actual.getDueDate()))
            .satisfies(e -> assertThat(e.getAmount()).as("check amount").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getAmount())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceThetaUpdatableRelationshipsEquals(InvoiceTheta expected, InvoiceTheta actual) {
        assertThat(expected)
            .as("Verify InvoiceTheta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
