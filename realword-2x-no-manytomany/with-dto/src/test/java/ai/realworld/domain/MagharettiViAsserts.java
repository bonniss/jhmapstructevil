package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MagharettiViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMagharettiViAllPropertiesEquals(MagharettiVi expected, MagharettiVi actual) {
        assertMagharettiViAutoGeneratedPropertiesEquals(expected, actual);
        assertMagharettiViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMagharettiViAllUpdatablePropertiesEquals(MagharettiVi expected, MagharettiVi actual) {
        assertMagharettiViUpdatableFieldsEquals(expected, actual);
        assertMagharettiViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMagharettiViAutoGeneratedPropertiesEquals(MagharettiVi expected, MagharettiVi actual) {
        assertThat(expected)
            .as("Verify MagharettiVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMagharettiViUpdatableFieldsEquals(MagharettiVi expected, MagharettiVi actual) {
        assertThat(expected)
            .as("Verify MagharettiVi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMagharettiViUpdatableRelationshipsEquals(MagharettiVi expected, MagharettiVi actual) {
        // empty method
    }
}
