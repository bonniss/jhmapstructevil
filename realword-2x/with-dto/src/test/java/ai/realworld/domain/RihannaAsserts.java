package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RihannaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaAllPropertiesEquals(Rihanna expected, Rihanna actual) {
        assertRihannaAutoGeneratedPropertiesEquals(expected, actual);
        assertRihannaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaAllUpdatablePropertiesEquals(Rihanna expected, Rihanna actual) {
        assertRihannaUpdatableFieldsEquals(expected, actual);
        assertRihannaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaAutoGeneratedPropertiesEquals(Rihanna expected, Rihanna actual) {
        assertThat(expected)
            .as("Verify Rihanna auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRihannaUpdatableFieldsEquals(Rihanna expected, Rihanna actual) {
        assertThat(expected)
            .as("Verify Rihanna relevant properties")
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
    public static void assertRihannaUpdatableRelationshipsEquals(Rihanna expected, Rihanna actual) {
        assertThat(expected)
            .as("Verify Rihanna relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
