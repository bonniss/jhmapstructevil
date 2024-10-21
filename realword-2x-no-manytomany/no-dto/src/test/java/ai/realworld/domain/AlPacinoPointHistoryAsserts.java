package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlPacinoPointHistoryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoPointHistoryAllPropertiesEquals(AlPacinoPointHistory expected, AlPacinoPointHistory actual) {
        assertAlPacinoPointHistoryAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPacinoPointHistoryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoPointHistoryAllUpdatablePropertiesEquals(AlPacinoPointHistory expected, AlPacinoPointHistory actual) {
        assertAlPacinoPointHistoryUpdatableFieldsEquals(expected, actual);
        assertAlPacinoPointHistoryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoPointHistoryAutoGeneratedPropertiesEquals(AlPacinoPointHistory expected, AlPacinoPointHistory actual) {
        assertThat(expected)
            .as("Verify AlPacinoPointHistory auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoPointHistoryUpdatableFieldsEquals(AlPacinoPointHistory expected, AlPacinoPointHistory actual) {
        assertThat(expected)
            .as("Verify AlPacinoPointHistory relevant properties")
            .satisfies(e -> assertThat(e.getSource()).as("check source").isEqualTo(actual.getSource()))
            .satisfies(e -> assertThat(e.getAssociatedId()).as("check associatedId").isEqualTo(actual.getAssociatedId()))
            .satisfies(e -> assertThat(e.getPointAmount()).as("check pointAmount").isEqualTo(actual.getPointAmount()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoPointHistoryUpdatableRelationshipsEquals(AlPacinoPointHistory expected, AlPacinoPointHistory actual) {
        assertThat(expected)
            .as("Verify AlPacinoPointHistory relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}