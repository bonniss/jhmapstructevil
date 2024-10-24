package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlVueVueViUsageAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueViUsageAllPropertiesEquals(AlVueVueViUsage expected, AlVueVueViUsage actual) {
        assertAlVueVueViUsageAutoGeneratedPropertiesEquals(expected, actual);
        assertAlVueVueViUsageAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueViUsageAllUpdatablePropertiesEquals(AlVueVueViUsage expected, AlVueVueViUsage actual) {
        assertAlVueVueViUsageUpdatableFieldsEquals(expected, actual);
        assertAlVueVueViUsageUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueViUsageAutoGeneratedPropertiesEquals(AlVueVueViUsage expected, AlVueVueViUsage actual) {
        assertThat(expected)
            .as("Verify AlVueVueViUsage auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueViUsageUpdatableFieldsEquals(AlVueVueViUsage expected, AlVueVueViUsage actual) {
        // empty method

    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlVueVueViUsageUpdatableRelationshipsEquals(AlVueVueViUsage expected, AlVueVueViUsage actual) {
        assertThat(expected)
            .as("Verify AlVueVueViUsage relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
