package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextReviewThetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewThetaAllPropertiesEquals(NextReviewTheta expected, NextReviewTheta actual) {
        assertNextReviewThetaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextReviewThetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewThetaAllUpdatablePropertiesEquals(NextReviewTheta expected, NextReviewTheta actual) {
        assertNextReviewThetaUpdatableFieldsEquals(expected, actual);
        assertNextReviewThetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewThetaAutoGeneratedPropertiesEquals(NextReviewTheta expected, NextReviewTheta actual) {
        assertThat(expected)
            .as("Verify NextReviewTheta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextReviewThetaUpdatableFieldsEquals(NextReviewTheta expected, NextReviewTheta actual) {
        assertThat(expected)
            .as("Verify NextReviewTheta relevant properties")
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
    public static void assertNextReviewThetaUpdatableRelationshipsEquals(NextReviewTheta expected, NextReviewTheta actual) {
        assertThat(expected)
            .as("Verify NextReviewTheta relationships")
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
