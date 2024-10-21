package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewViViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewViViAllPropertiesEquals(ReviewViVi expected, ReviewViVi actual) {
        assertReviewViViAutoGeneratedPropertiesEquals(expected, actual);
        assertReviewViViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewViViAllUpdatablePropertiesEquals(ReviewViVi expected, ReviewViVi actual) {
        assertReviewViViUpdatableFieldsEquals(expected, actual);
        assertReviewViViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewViViAutoGeneratedPropertiesEquals(ReviewViVi expected, ReviewViVi actual) {
        assertThat(expected)
            .as("Verify ReviewViVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewViViUpdatableFieldsEquals(ReviewViVi expected, ReviewViVi actual) {
        assertThat(expected)
            .as("Verify ReviewViVi relevant properties")
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
    public static void assertReviewViViUpdatableRelationshipsEquals(ReviewViVi expected, ReviewViVi actual) {
        assertThat(expected)
            .as("Verify ReviewViVi relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()));
    }
}
