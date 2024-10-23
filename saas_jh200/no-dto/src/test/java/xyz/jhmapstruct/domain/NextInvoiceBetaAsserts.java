package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class NextInvoiceBetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceBetaAllPropertiesEquals(NextInvoiceBeta expected, NextInvoiceBeta actual) {
        assertNextInvoiceBetaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextInvoiceBetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceBetaAllUpdatablePropertiesEquals(NextInvoiceBeta expected, NextInvoiceBeta actual) {
        assertNextInvoiceBetaUpdatableFieldsEquals(expected, actual);
        assertNextInvoiceBetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceBetaAutoGeneratedPropertiesEquals(NextInvoiceBeta expected, NextInvoiceBeta actual) {
        assertThat(expected)
            .as("Verify NextInvoiceBeta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceBetaUpdatableFieldsEquals(NextInvoiceBeta expected, NextInvoiceBeta actual) {
        assertThat(expected)
            .as("Verify NextInvoiceBeta relevant properties")
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
    public static void assertNextInvoiceBetaUpdatableRelationshipsEquals(NextInvoiceBeta expected, NextInvoiceBeta actual) {
        assertThat(expected)
            .as("Verify NextInvoiceBeta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
