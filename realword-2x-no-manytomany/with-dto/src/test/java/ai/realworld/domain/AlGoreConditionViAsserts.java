package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlGoreConditionViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoreConditionViAllPropertiesEquals(AlGoreConditionVi expected, AlGoreConditionVi actual) {
        assertAlGoreConditionViAutoGeneratedPropertiesEquals(expected, actual);
        assertAlGoreConditionViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoreConditionViAllUpdatablePropertiesEquals(AlGoreConditionVi expected, AlGoreConditionVi actual) {
        assertAlGoreConditionViUpdatableFieldsEquals(expected, actual);
        assertAlGoreConditionViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoreConditionViAutoGeneratedPropertiesEquals(AlGoreConditionVi expected, AlGoreConditionVi actual) {
        assertThat(expected)
            .as("Verify AlGoreConditionVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoreConditionViUpdatableFieldsEquals(AlGoreConditionVi expected, AlGoreConditionVi actual) {
        assertThat(expected)
            .as("Verify AlGoreConditionVi relevant properties")
            .satisfies(e -> assertThat(e.getSubjectType()).as("check subjectType").isEqualTo(actual.getSubjectType()))
            .satisfies(e -> assertThat(e.getSubject()).as("check subject").isEqualTo(actual.getSubject()))
            .satisfies(e -> assertThat(e.getAction()).as("check action").isEqualTo(actual.getAction()))
            .satisfies(e -> assertThat(e.getNote()).as("check note").isEqualTo(actual.getNote()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoreConditionViUpdatableRelationshipsEquals(AlGoreConditionVi expected, AlGoreConditionVi actual) {
        assertThat(expected)
            .as("Verify AlGoreConditionVi relationships")
            .satisfies(e -> assertThat(e.getParent()).as("check parent").isEqualTo(actual.getParent()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}