package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeViAllPropertiesEquals(EmployeeVi expected, EmployeeVi actual) {
        assertEmployeeViAutoGeneratedPropertiesEquals(expected, actual);
        assertEmployeeViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeViAllUpdatablePropertiesEquals(EmployeeVi expected, EmployeeVi actual) {
        assertEmployeeViUpdatableFieldsEquals(expected, actual);
        assertEmployeeViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeViAutoGeneratedPropertiesEquals(EmployeeVi expected, EmployeeVi actual) {
        assertThat(expected)
            .as("Verify EmployeeVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeViUpdatableFieldsEquals(EmployeeVi expected, EmployeeVi actual) {
        assertThat(expected)
            .as("Verify EmployeeVi relevant properties")
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
    public static void assertEmployeeViUpdatableRelationshipsEquals(EmployeeVi expected, EmployeeVi actual) {
        assertThat(expected)
            .as("Verify EmployeeVi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
