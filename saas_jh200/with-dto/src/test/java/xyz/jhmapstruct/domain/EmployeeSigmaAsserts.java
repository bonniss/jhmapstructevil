package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeSigmaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeSigmaAllPropertiesEquals(EmployeeSigma expected, EmployeeSigma actual) {
        assertEmployeeSigmaAutoGeneratedPropertiesEquals(expected, actual);
        assertEmployeeSigmaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeSigmaAllUpdatablePropertiesEquals(EmployeeSigma expected, EmployeeSigma actual) {
        assertEmployeeSigmaUpdatableFieldsEquals(expected, actual);
        assertEmployeeSigmaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeSigmaAutoGeneratedPropertiesEquals(EmployeeSigma expected, EmployeeSigma actual) {
        assertThat(expected)
            .as("Verify EmployeeSigma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeSigmaUpdatableFieldsEquals(EmployeeSigma expected, EmployeeSigma actual) {
        assertThat(expected)
            .as("Verify EmployeeSigma relevant properties")
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
    public static void assertEmployeeSigmaUpdatableRelationshipsEquals(EmployeeSigma expected, EmployeeSigma actual) {
        assertThat(expected)
            .as("Verify EmployeeSigma relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}