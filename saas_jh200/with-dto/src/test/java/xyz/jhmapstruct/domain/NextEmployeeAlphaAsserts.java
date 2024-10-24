package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextEmployeeAlphaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeAlphaAllPropertiesEquals(NextEmployeeAlpha expected, NextEmployeeAlpha actual) {
        assertNextEmployeeAlphaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextEmployeeAlphaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeAlphaAllUpdatablePropertiesEquals(NextEmployeeAlpha expected, NextEmployeeAlpha actual) {
        assertNextEmployeeAlphaUpdatableFieldsEquals(expected, actual);
        assertNextEmployeeAlphaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeAlphaAutoGeneratedPropertiesEquals(NextEmployeeAlpha expected, NextEmployeeAlpha actual) {
        assertThat(expected)
            .as("Verify NextEmployeeAlpha auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextEmployeeAlphaUpdatableFieldsEquals(NextEmployeeAlpha expected, NextEmployeeAlpha actual) {
        assertThat(expected)
            .as("Verify NextEmployeeAlpha relevant properties")
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
    public static void assertNextEmployeeAlphaUpdatableRelationshipsEquals(NextEmployeeAlpha expected, NextEmployeeAlpha actual) {
        assertThat(expected)
            .as("Verify NextEmployeeAlpha relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
