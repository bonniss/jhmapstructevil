package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextEmployeeViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeViAllPropertiesEquals(NextEmployeeVi expected, NextEmployeeVi actual) {
        assertNextEmployeeViAutoGeneratedPropertiesEquals(expected, actual);
        assertNextEmployeeViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeViAllUpdatablePropertiesEquals(NextEmployeeVi expected, NextEmployeeVi actual) {
        assertNextEmployeeViUpdatableFieldsEquals(expected, actual);
        assertNextEmployeeViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeViAutoGeneratedPropertiesEquals(NextEmployeeVi expected, NextEmployeeVi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeViUpdatableFieldsEquals(NextEmployeeVi expected, NextEmployeeVi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeVi relevant properties")
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
    public static void assertNextEmployeeViUpdatableRelationshipsEquals(NextEmployeeVi expected, NextEmployeeVi actual) {
        assertThat(expected)
            .as("Verify NextEmployeeVi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}