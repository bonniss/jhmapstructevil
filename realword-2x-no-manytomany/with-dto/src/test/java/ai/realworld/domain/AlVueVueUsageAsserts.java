package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlVueVueUsageAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueUsageAllPropertiesEquals(AlVueVueUsage expected, AlVueVueUsage actual) {
        assertAlVueVueUsageAutoGeneratedPropertiesEquals(expected, actual);
        assertAlVueVueUsageAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueUsageAllUpdatablePropertiesEquals(AlVueVueUsage expected, AlVueVueUsage actual) {
        assertAlVueVueUsageUpdatableFieldsEquals(expected, actual);
        assertAlVueVueUsageUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueUsageAutoGeneratedPropertiesEquals(AlVueVueUsage expected, AlVueVueUsage actual) {
        assertThat(expected)
            .as("Verify AlVueVueUsage auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueUsageUpdatableFieldsEquals(AlVueVueUsage expected, AlVueVueUsage actual) {
        // empty method

    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueUsageUpdatableRelationshipsEquals(AlVueVueUsage expected, AlVueVueUsage actual) {
        assertThat(expected)
            .as("Verify AlVueVueUsage relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
