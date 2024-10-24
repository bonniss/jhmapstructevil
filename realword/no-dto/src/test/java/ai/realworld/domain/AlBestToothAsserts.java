package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlBestToothAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBestToothAllPropertiesEquals(AlBestTooth expected, AlBestTooth actual) {
        assertAlBestToothAutoGeneratedPropertiesEquals(expected, actual);
        assertAlBestToothAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBestToothAllUpdatablePropertiesEquals(AlBestTooth expected, AlBestTooth actual) {
        assertAlBestToothUpdatableFieldsEquals(expected, actual);
        assertAlBestToothUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBestToothAutoGeneratedPropertiesEquals(AlBestTooth expected, AlBestTooth actual) {
        assertThat(expected)
            .as("Verify AlBestTooth auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBestToothUpdatableFieldsEquals(AlBestTooth expected, AlBestTooth actual) {
        assertThat(expected)
            .as("Verify AlBestTooth relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBestToothUpdatableRelationshipsEquals(AlBestTooth expected, AlBestTooth actual) {
        assertThat(expected)
            .as("Verify AlBestTooth relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()))
            .satisfies(e -> assertThat(e.getArticles()).as("check articles").isEqualTo(actual.getArticles()));
    }
}
