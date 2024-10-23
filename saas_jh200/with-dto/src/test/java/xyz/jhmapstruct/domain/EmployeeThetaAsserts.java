package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeThetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeThetaAllPropertiesEquals(EmployeeTheta expected, EmployeeTheta actual) {
        assertEmployeeThetaAutoGeneratedPropertiesEquals(expected, actual);
        assertEmployeeThetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeThetaAllUpdatablePropertiesEquals(EmployeeTheta expected, EmployeeTheta actual) {
        assertEmployeeThetaUpdatableFieldsEquals(expected, actual);
        assertEmployeeThetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeThetaAutoGeneratedPropertiesEquals(EmployeeTheta expected, EmployeeTheta actual) {
        assertThat(expected)
            .as("Verify EmployeeTheta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeThetaUpdatableFieldsEquals(EmployeeTheta expected, EmployeeTheta actual) {
        assertThat(expected)
            .as("Verify EmployeeTheta relevant properties")
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
    public static void assertEmployeeThetaUpdatableRelationshipsEquals(EmployeeTheta expected, EmployeeTheta actual) {
        assertThat(expected)
            .as("Verify EmployeeTheta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
