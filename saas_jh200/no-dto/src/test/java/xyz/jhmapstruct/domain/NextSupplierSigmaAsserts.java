package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextSupplierSigmaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierSigmaAllPropertiesEquals(NextSupplierSigma expected, NextSupplierSigma actual) {
        assertNextSupplierSigmaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextSupplierSigmaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierSigmaAllUpdatablePropertiesEquals(NextSupplierSigma expected, NextSupplierSigma actual) {
        assertNextSupplierSigmaUpdatableFieldsEquals(expected, actual);
        assertNextSupplierSigmaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierSigmaAutoGeneratedPropertiesEquals(NextSupplierSigma expected, NextSupplierSigma actual) {
        assertThat(expected)
            .as("Verify NextSupplierSigma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierSigmaUpdatableFieldsEquals(NextSupplierSigma expected, NextSupplierSigma actual) {
        assertThat(expected)
            .as("Verify NextSupplierSigma relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getContactPerson()).as("check contactPerson").isEqualTo(actual.getContactPerson()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPhoneNumber()).as("check phoneNumber").isEqualTo(actual.getPhoneNumber()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierSigmaUpdatableRelationshipsEquals(NextSupplierSigma expected, NextSupplierSigma actual) {
        assertThat(expected)
            .as("Verify NextSupplierSigma relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()))
            .satisfies(e -> assertThat(e.getProducts()).as("check products").isEqualTo(actual.getProducts()));
    }
}
