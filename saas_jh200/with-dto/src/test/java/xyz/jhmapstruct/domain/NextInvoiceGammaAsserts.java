package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.AssertUtils.bigDecimalCompareTo;

public class NextInvoiceGammaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceGammaAllPropertiesEquals(NextInvoiceGamma expected, NextInvoiceGamma actual) {
        assertNextInvoiceGammaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextInvoiceGammaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceGammaAllUpdatablePropertiesEquals(NextInvoiceGamma expected, NextInvoiceGamma actual) {
        assertNextInvoiceGammaUpdatableFieldsEquals(expected, actual);
        assertNextInvoiceGammaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceGammaAutoGeneratedPropertiesEquals(NextInvoiceGamma expected, NextInvoiceGamma actual) {
        assertThat(expected)
            .as("Verify NextInvoiceGamma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextInvoiceGammaUpdatableFieldsEquals(NextInvoiceGamma expected, NextInvoiceGamma actual) {
        assertThat(expected)
            .as("Verify NextInvoiceGamma relevant properties")
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
    public static void assertNextInvoiceGammaUpdatableRelationshipsEquals(NextInvoiceGamma expected, NextInvoiceGamma actual) {
        assertThat(expected)
            .as("Verify NextInvoiceGamma relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
