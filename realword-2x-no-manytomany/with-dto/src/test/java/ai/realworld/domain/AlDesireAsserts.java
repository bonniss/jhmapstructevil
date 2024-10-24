package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlDesireAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlDesireAllPropertiesEquals(AlDesire expected, AlDesire actual) {
        assertAlDesireAutoGeneratedPropertiesEquals(expected, actual);
        assertAlDesireAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlDesireAllUpdatablePropertiesEquals(AlDesire expected, AlDesire actual) {
        assertAlDesireUpdatableFieldsEquals(expected, actual);
        assertAlDesireUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlDesireAutoGeneratedPropertiesEquals(AlDesire expected, AlDesire actual) {
        assertThat(expected)
            .as("Verify AlDesire auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlDesireUpdatableFieldsEquals(AlDesire expected, AlDesire actual) {
        assertThat(expected)
            .as("Verify AlDesire relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getWeight()).as("check weight").isEqualTo(actual.getWeight()))
            .satisfies(e ->
                assertThat(e.getProbabilityOfWinning()).as("check probabilityOfWinning").isEqualTo(actual.getProbabilityOfWinning())
            )
            .satisfies(e -> assertThat(e.getMaximumWinningTime()).as("check maximumWinningTime").isEqualTo(actual.getMaximumWinningTime()))
            .satisfies(e ->
                assertThat(e.getIsWinningTimeLimited()).as("check isWinningTimeLimited").isEqualTo(actual.getIsWinningTimeLimited())
            )
            .satisfies(e -> assertThat(e.getAwardResultType()).as("check awardResultType").isEqualTo(actual.getAwardResultType()))
            .satisfies(e -> assertThat(e.getAwardReference()).as("check awardReference").isEqualTo(actual.getAwardReference()))
            .satisfies(e -> assertThat(e.getIsDefault()).as("check isDefault").isEqualTo(actual.getIsDefault()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlDesireUpdatableRelationshipsEquals(AlDesire expected, AlDesire actual) {
        assertThat(expected)
            .as("Verify AlDesire relationships")
            .satisfies(e -> assertThat(e.getImage()).as("check image").isEqualTo(actual.getImage()))
            .satisfies(e -> assertThat(e.getMaggi()).as("check maggi").isEqualTo(actual.getMaggi()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
