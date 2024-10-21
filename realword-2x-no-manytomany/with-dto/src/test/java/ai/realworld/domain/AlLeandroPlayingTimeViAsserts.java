package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlLeandroPlayingTimeViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLeandroPlayingTimeViAllPropertiesEquals(AlLeandroPlayingTimeVi expected, AlLeandroPlayingTimeVi actual) {
        assertAlLeandroPlayingTimeViAutoGeneratedPropertiesEquals(expected, actual);
        assertAlLeandroPlayingTimeViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLeandroPlayingTimeViAllUpdatablePropertiesEquals(
        AlLeandroPlayingTimeVi expected,
        AlLeandroPlayingTimeVi actual
    ) {
        assertAlLeandroPlayingTimeViUpdatableFieldsEquals(expected, actual);
        assertAlLeandroPlayingTimeViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLeandroPlayingTimeViAutoGeneratedPropertiesEquals(
        AlLeandroPlayingTimeVi expected,
        AlLeandroPlayingTimeVi actual
    ) {
        assertThat(expected)
            .as("Verify AlLeandroPlayingTimeVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLeandroPlayingTimeViUpdatableFieldsEquals(AlLeandroPlayingTimeVi expected, AlLeandroPlayingTimeVi actual) {
        assertThat(expected)
            .as("Verify AlLeandroPlayingTimeVi relevant properties")
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getWonDate()).as("check wonDate").isEqualTo(actual.getWonDate()))
            .satisfies(e ->
                assertThat(e.getSentAwardToPlayerAt()).as("check sentAwardToPlayerAt").isEqualTo(actual.getSentAwardToPlayerAt())
            )
            .satisfies(e ->
                assertThat(e.getSentAwardToPlayerBy()).as("check sentAwardToPlayerBy").isEqualTo(actual.getSentAwardToPlayerBy())
            )
            .satisfies(e ->
                assertThat(e.getPlayerReceivedTheAwardAt())
                    .as("check playerReceivedTheAwardAt")
                    .isEqualTo(actual.getPlayerReceivedTheAwardAt())
            )
            .satisfies(e -> assertThat(e.getPlaySourceTime()).as("check playSourceTime").isEqualTo(actual.getPlaySourceTime()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLeandroPlayingTimeViUpdatableRelationshipsEquals(
        AlLeandroPlayingTimeVi expected,
        AlLeandroPlayingTimeVi actual
    ) {
        assertThat(expected)
            .as("Verify AlLeandroPlayingTimeVi relationships")
            .satisfies(e -> assertThat(e.getMaggi()).as("check maggi").isEqualTo(actual.getMaggi()))
            .satisfies(e -> assertThat(e.getUser()).as("check user").isEqualTo(actual.getUser()))
            .satisfies(e -> assertThat(e.getAward()).as("check award").isEqualTo(actual.getAward()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
