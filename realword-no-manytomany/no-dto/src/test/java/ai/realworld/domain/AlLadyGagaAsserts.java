package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlLadyGagaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaAllPropertiesEquals(AlLadyGaga expected, AlLadyGaga actual) {
        assertAlLadyGagaAutoGeneratedPropertiesEquals(expected, actual);
        assertAlLadyGagaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaAllUpdatablePropertiesEquals(AlLadyGaga expected, AlLadyGaga actual) {
        assertAlLadyGagaUpdatableFieldsEquals(expected, actual);
        assertAlLadyGagaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaAutoGeneratedPropertiesEquals(AlLadyGaga expected, AlLadyGaga actual) {
        assertThat(expected)
            .as("Verify AlLadyGaga auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaUpdatableFieldsEquals(AlLadyGaga expected, AlLadyGaga actual) {
        assertThat(expected)
            .as("Verify AlLadyGaga relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescriptionHeitiga()).as("check descriptionHeitiga").isEqualTo(actual.getDescriptionHeitiga()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaUpdatableRelationshipsEquals(AlLadyGaga expected, AlLadyGaga actual) {
        assertThat(expected)
            .as("Verify AlLadyGaga relationships")
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getAvatar()).as("check avatar").isEqualTo(actual.getAvatar()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
