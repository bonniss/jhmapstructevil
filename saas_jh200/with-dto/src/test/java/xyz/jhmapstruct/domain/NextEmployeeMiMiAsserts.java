package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextEmployeeMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiMiAllPropertiesEquals(NextEmployeeMiMi expected, NextEmployeeMiMi actual) {
        assertNextEmployeeMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertNextEmployeeMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiMiAllUpdatablePropertiesEquals(NextEmployeeMiMi expected, NextEmployeeMiMi actual) {
        assertNextEmployeeMiMiUpdatableFieldsEquals(expected, actual);
        assertNextEmployeeMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiMiAutoGeneratedPropertiesEquals(NextEmployeeMiMi expected, NextEmployeeMiMi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeMiMiUpdatableFieldsEquals(NextEmployeeMiMi expected, NextEmployeeMiMi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeMiMi relevant properties")
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
    public static void assertNextEmployeeMiMiUpdatableRelationshipsEquals(NextEmployeeMiMi expected, NextEmployeeMiMi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeMiMi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
