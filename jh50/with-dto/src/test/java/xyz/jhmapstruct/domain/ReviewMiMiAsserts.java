package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewMiMiAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewMiMiAllPropertiesEquals(ReviewMiMi expected, ReviewMiMi actual) {
        assertReviewMiMiAutoGeneratedPropertiesEquals(expected, actual);
        assertReviewMiMiAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewMiMiAllUpdatablePropertiesEquals(ReviewMiMi expected, ReviewMiMi actual) {
        assertReviewMiMiUpdatableFieldsEquals(expected, actual);
        assertReviewMiMiUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewMiMiAutoGeneratedPropertiesEquals(ReviewMiMi expected, ReviewMiMi actual) {
        assertThat(expected)
            .as("Verify ReviewMiMi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReviewMiMiUpdatableFieldsEquals(ReviewMiMi expected, ReviewMiMi actual) {
        assertThat(expected)
            .as("Verify ReviewMiMi relevant properties")
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
    public static void assertReviewMiMiUpdatableRelationshipsEquals(ReviewMiMi expected, ReviewMiMi actual) {
        assertThat(expected)
            .as("Verify ReviewMiMi relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()));
    }
}
