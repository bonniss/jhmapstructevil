package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextCustomerMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerMiMiAllPropertiesEquals(NextCustomerMiMi expected, NextCustomerMiMi actual) {
        assertNextCustomerMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertNextCustomerMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerMiMiAllUpdatablePropertiesEquals(NextCustomerMiMi expected, NextCustomerMiMi actual) {
        assertNextCustomerMiMiUpdatableFieldsEquals(expected, actual);
        assertNextCustomerMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerMiMiAutoGeneratedPropertiesEquals(NextCustomerMiMi expected, NextCustomerMiMi actual) {
        assertThat(expected)
            .as("Verify NextCustomerMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCustomerMiMiUpdatableFieldsEquals(NextCustomerMiMi expected, NextCustomerMiMi actual) {
        assertThat(expected)
            .as("Verify NextCustomerMiMi relevant properties")
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
    public static void assertNextCustomerMiMiUpdatableRelationshipsEquals(NextCustomerMiMi expected, NextCustomerMiMi actual) {
        assertThat(expected)
            .as("Verify NextCustomerMiMi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
