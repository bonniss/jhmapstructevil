package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryViViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryViViAllPropertiesEquals(CategoryViVi expected, CategoryViVi actual) {
        assertCategoryViViAutoGeneratedPropertiesEquals(expected, actual);
        assertCategoryViViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryViViAllUpdatablePropertiesEquals(CategoryViVi expected, CategoryViVi actual) {
        assertCategoryViViUpdatableFieldsEquals(expected, actual);
        assertCategoryViViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryViViAutoGeneratedPropertiesEquals(CategoryViVi expected, CategoryViVi actual) {
        assertThat(expected)
            .as("Verify CategoryViVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryViViUpdatableFieldsEquals(CategoryViVi expected, CategoryViVi actual) {
        assertThat(expected)
            .as("Verify CategoryViVi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCategoryViViUpdatableRelationshipsEquals(CategoryViVi expected, CategoryViVi actual) {
        // empty method
    }
}