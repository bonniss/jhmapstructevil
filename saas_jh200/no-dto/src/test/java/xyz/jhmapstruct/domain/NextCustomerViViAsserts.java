package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextCustomerViViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerViViAllPropertiesEquals(NextCustomerViVi expected, NextCustomerViVi actual) {
        assertNextCustomerViViAutoGeneratedPropertiesEquals(expected, actual);
        assertNextCustomerViViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerViViAllUpdatablePropertiesEquals(NextCustomerViVi expected, NextCustomerViVi actual) {
        assertNextCustomerViViUpdatableFieldsEquals(expected, actual);
        assertNextCustomerViViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerViViAutoGeneratedPropertiesEquals(NextCustomerViVi expected, NextCustomerViVi actual) {
        assertThat(expected)
            .as("Verify NextCustomerViVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerViViUpdatableFieldsEquals(NextCustomerViVi expected, NextCustomerViVi actual) {
        assertThat(expected)
            .as("Verify NextCustomerViVi relevant properties")
            .satisfies(e -> assertThat(e.getFirstName()).as("check firstName").isEqualTo(actual.getFirstName()))
            .satisfies(e -> assertThat(e.getLastName()).as("check lastName").isEqualTo(actual.getLastName()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPhoneNumber()).as("check phoneNumber").isEqualTo(actual.getPhoneNumber()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerViViUpdatableRelationshipsEquals(NextCustomerViVi expected, NextCustomerViVi actual) {
        assertThat(expected)
            .as("Verify NextCustomerViVi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
