package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewSigmaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewSigmaAllPropertiesEquals(ReviewSigma expected, ReviewSigma actual) {
        assertReviewSigmaAutoGeneratedPropertiesEquals(expected, actual);
        assertReviewSigmaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewSigmaAllUpdatablePropertiesEquals(ReviewSigma expected, ReviewSigma actual) {
        assertReviewSigmaUpdatableFieldsEquals(expected, actual);
        assertReviewSigmaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewSigmaAutoGeneratedPropertiesEquals(ReviewSigma expected, ReviewSigma actual) {
        assertThat(expected)
            .as("Verify ReviewSigma auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewSigmaUpdatableFieldsEquals(ReviewSigma expected, ReviewSigma actual) {
        assertThat(expected)
            .as("Verify ReviewSigma relevant properties")
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
    public static void assertReviewSigmaUpdatableRelationshipsEquals(ReviewSigma expected, ReviewSigma actual) {
        assertThat(expected)
            .as("Verify ReviewSigma relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
