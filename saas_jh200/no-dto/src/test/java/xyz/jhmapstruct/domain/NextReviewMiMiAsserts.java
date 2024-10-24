package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextReviewMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewMiMiAllPropertiesEquals(NextReviewMiMi expected, NextReviewMiMi actual) {
        assertNextReviewMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertNextReviewMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewMiMiAllUpdatablePropertiesEquals(NextReviewMiMi expected, NextReviewMiMi actual) {
        assertNextReviewMiMiUpdatableFieldsEquals(expected, actual);
        assertNextReviewMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewMiMiAutoGeneratedPropertiesEquals(NextReviewMiMi expected, NextReviewMiMi actual) {
        assertThat(expected)
            .as("Verify NextReviewMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewMiMiUpdatableFieldsEquals(NextReviewMiMi expected, NextReviewMiMi actual) {
        assertThat(expected)
            .as("Verify NextReviewMiMi relevant properties")
            .satisfies(e -> assertThat(e.getRating()).as("check rating").isEqualTo(actual.getRating()))
            .satisfies(e -> assertThat(e.getComment()).as("check comment").isEqualTo(actual.getComment()))
            .satisfies(e -> assertThat(e.getReviewDate()).as("check reviewDate").isEqualTo(actual.getReviewDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewMiMiUpdatableRelationshipsEquals(NextReviewMiMi expected, NextReviewMiMi actual) {
        assertThat(expected)
            .as("Verify NextReviewMiMi relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
