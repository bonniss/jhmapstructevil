package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextEmployeeMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiAllPropertiesEquals(NextEmployeeMi expected, NextEmployeeMi actual) {
        assertNextEmployeeMiAutoGeneratedPropertiesEquals(expected, actual);
        assertNextEmployeeMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiAllUpdatablePropertiesEquals(NextEmployeeMi expected, NextEmployeeMi actual) {
        assertNextEmployeeMiUpdatableFieldsEquals(expected, actual);
        assertNextEmployeeMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiAutoGeneratedPropertiesEquals(NextEmployeeMi expected, NextEmployeeMi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiUpdatableFieldsEquals(NextEmployeeMi expected, NextEmployeeMi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeMi relevant properties")
            .satisfies(e -> assertThat(e.getFirstName()).as("check firstName").isEqualTo(actual.getFirstName()))
            .satisfies(e -> assertThat(e.getLastName()).as("check lastName").isEqualTo(actual.getLastName()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getHireDate()).as("check hireDate").isEqualTo(actual.getHireDate()))
            .satisfies(e -> assertThat(e.getPosition()).as("check position").isEqualTo(actual.getPosition()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiUpdatableRelationshipsEquals(NextEmployeeMi expected, NextEmployeeMi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeMi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
