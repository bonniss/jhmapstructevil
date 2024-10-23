package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextCategoryMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCategoryMiAllPropertiesEquals(NextCategoryMi expected, NextCategoryMi actual) {
        assertNextCategoryMiAutoGeneratedPropertiesEquals(expected, actual);
        assertNextCategoryMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCategoryMiAllUpdatablePropertiesEquals(NextCategoryMi expected, NextCategoryMi actual) {
        assertNextCategoryMiUpdatableFieldsEquals(expected, actual);
        assertNextCategoryMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCategoryMiAutoGeneratedPropertiesEquals(NextCategoryMi expected, NextCategoryMi actual) {
        assertThat(expected)
            .as("Verify NextCategoryMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCategoryMiUpdatableFieldsEquals(NextCategoryMi expected, NextCategoryMi actual) {
        assertThat(expected)
            .as("Verify NextCategoryMi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextCategoryMiUpdatableRelationshipsEquals(NextCategoryMi expected, NextCategoryMi actual) {
        assertThat(expected)
            .as("Verify NextCategoryMi relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}