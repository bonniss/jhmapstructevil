package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlBetonamuRelationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBetonamuRelationAllPropertiesEquals(AlBetonamuRelation expected, AlBetonamuRelation actual) {
        assertAlBetonamuRelationAutoGeneratedPropertiesEquals(expected, actual);
        assertAlBetonamuRelationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBetonamuRelationAllUpdatablePropertiesEquals(AlBetonamuRelation expected, AlBetonamuRelation actual) {
        assertAlBetonamuRelationUpdatableFieldsEquals(expected, actual);
        assertAlBetonamuRelationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBetonamuRelationAutoGeneratedPropertiesEquals(AlBetonamuRelation expected, AlBetonamuRelation actual) {
        assertThat(expected)
            .as("Verify AlBetonamuRelation auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBetonamuRelationUpdatableFieldsEquals(AlBetonamuRelation expected, AlBetonamuRelation actual) {
        assertThat(expected)
            .as("Verify AlBetonamuRelation relevant properties")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlBetonamuRelationUpdatableRelationshipsEquals(AlBetonamuRelation expected, AlBetonamuRelation actual) {
        assertThat(expected)
            .as("Verify AlBetonamuRelation relationships")
            .satisfies(e -> assertThat(e.getSupplier()).as("check supplier").isEqualTo(actual.getSupplier()))
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}