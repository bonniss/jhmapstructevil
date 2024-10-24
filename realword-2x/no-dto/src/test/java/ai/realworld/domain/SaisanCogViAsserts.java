package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SaisanCogViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSaisanCogViAllPropertiesEquals(SaisanCogVi expected, SaisanCogVi actual) {
        assertSaisanCogViAutoGeneratedPropertiesEquals(expected, actual);
        assertSaisanCogViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSaisanCogViAllUpdatablePropertiesEquals(SaisanCogVi expected, SaisanCogVi actual) {
        assertSaisanCogViUpdatableFieldsEquals(expected, actual);
        assertSaisanCogViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSaisanCogViAutoGeneratedPropertiesEquals(SaisanCogVi expected, SaisanCogVi actual) {
        assertThat(expected)
            .as("Verify SaisanCogVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSaisanCogViUpdatableFieldsEquals(SaisanCogVi expected, SaisanCogVi actual) {
        assertThat(expected)
            .as("Verify SaisanCogVi relevant properties")
            .satisfies(e -> assertThat(e.getKey()).as("check key").isEqualTo(actual.getKey()))
            .satisfies(e -> assertThat(e.getValueJason()).as("check valueJason").isEqualTo(actual.getValueJason()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSaisanCogViUpdatableRelationshipsEquals(SaisanCogVi expected, SaisanCogVi actual) {
        // empty method
    }
}
