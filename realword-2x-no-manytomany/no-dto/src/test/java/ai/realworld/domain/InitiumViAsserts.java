package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class InitiumViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInitiumViAllPropertiesEquals(InitiumVi expected, InitiumVi actual) {
        assertInitiumViAutoGeneratedPropertiesEquals(expected, actual);
        assertInitiumViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInitiumViAllUpdatablePropertiesEquals(InitiumVi expected, InitiumVi actual) {
        assertInitiumViUpdatableFieldsEquals(expected, actual);
        assertInitiumViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInitiumViAutoGeneratedPropertiesEquals(InitiumVi expected, InitiumVi actual) {
        assertThat(expected)
            .as("Verify InitiumVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInitiumViUpdatableFieldsEquals(InitiumVi expected, InitiumVi actual) {
        assertThat(expected)
            .as("Verify InitiumVi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getSlug()).as("check slug").isEqualTo(actual.getSlug()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getIsJelloSupported()).as("check isJelloSupported").isEqualTo(actual.getIsJelloSupported()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInitiumViUpdatableRelationshipsEquals(InitiumVi expected, InitiumVi actual) {
        // empty method
    }
}
