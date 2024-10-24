package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlLexFergAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLexFergAllPropertiesEquals(AlLexFerg expected, AlLexFerg actual) {
        assertAlLexFergAutoGeneratedPropertiesEquals(expected, actual);
        assertAlLexFergAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLexFergAllUpdatablePropertiesEquals(AlLexFerg expected, AlLexFerg actual) {
        assertAlLexFergUpdatableFieldsEquals(expected, actual);
        assertAlLexFergUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLexFergAutoGeneratedPropertiesEquals(AlLexFerg expected, AlLexFerg actual) {
        assertThat(expected)
            .as("Verify AlLexFerg auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLexFergUpdatableFieldsEquals(AlLexFerg expected, AlLexFerg actual) {
        assertThat(expected)
            .as("Verify AlLexFerg relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getSlug()).as("check slug").isEqualTo(actual.getSlug()))
            .satisfies(e -> assertThat(e.getSummary()).as("check summary").isEqualTo(actual.getSummary()))
            .satisfies(e -> assertThat(e.getContentHeitiga()).as("check contentHeitiga").isEqualTo(actual.getContentHeitiga()))
            .satisfies(e -> assertThat(e.getPublicationStatus()).as("check publicationStatus").isEqualTo(actual.getPublicationStatus()))
            .satisfies(e -> assertThat(e.getPublishedDate()).as("check publishedDate").isEqualTo(actual.getPublishedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLexFergUpdatableRelationshipsEquals(AlLexFerg expected, AlLexFerg actual) {
        assertThat(expected)
            .as("Verify AlLexFerg relationships")
            .satisfies(e -> assertThat(e.getAvatar()).as("check avatar").isEqualTo(actual.getAvatar()))
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
