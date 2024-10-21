package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RihannaViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaViAllPropertiesEquals(RihannaVi expected, RihannaVi actual) {
        assertRihannaViAutoGeneratedPropertiesEquals(expected, actual);
        assertRihannaViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaViAllUpdatablePropertiesEquals(RihannaVi expected, RihannaVi actual) {
        assertRihannaViUpdatableFieldsEquals(expected, actual);
        assertRihannaViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaViAutoGeneratedPropertiesEquals(RihannaVi expected, RihannaVi actual) {
        assertThat(expected)
            .as("Verify RihannaVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaViUpdatableFieldsEquals(RihannaVi expected, RihannaVi actual) {
        assertThat(expected)
            .as("Verify RihannaVi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e ->
                assertThat(e.getPermissionGridJason()).as("check permissionGridJason").isEqualTo(actual.getPermissionGridJason())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaViUpdatableRelationshipsEquals(RihannaVi expected, RihannaVi actual) {
        assertThat(expected)
            .as("Verify RihannaVi relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}