package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlProtyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlProtyAllPropertiesEquals(AlProty expected, AlProty actual) {
        assertAlProtyAutoGeneratedPropertiesEquals(expected, actual);
        assertAlProtyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlProtyAllUpdatablePropertiesEquals(AlProty expected, AlProty actual) {
        assertAlProtyUpdatableFieldsEquals(expected, actual);
        assertAlProtyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlProtyAutoGeneratedPropertiesEquals(AlProty expected, AlProty actual) {
        assertThat(expected)
            .as("Verify AlProty auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlProtyUpdatableFieldsEquals(AlProty expected, AlProty actual) {
        assertThat(expected)
            .as("Verify AlProty relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescriptionHeitiga()).as("check descriptionHeitiga").isEqualTo(actual.getDescriptionHeitiga()))
            .satisfies(e -> assertThat(e.getCoordinate()).as("check coordinate").isEqualTo(actual.getCoordinate()))
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlProtyUpdatableRelationshipsEquals(AlProty expected, AlProty actual) {
        assertThat(expected)
            .as("Verify AlProty relationships")
            .satisfies(e -> assertThat(e.getParent()).as("check parent").isEqualTo(actual.getParent()))
            .satisfies(e -> assertThat(e.getOperator()).as("check operator").isEqualTo(actual.getOperator()))
            .satisfies(e -> assertThat(e.getPropertyProfile()).as("check propertyProfile").isEqualTo(actual.getPropertyProfile()))
            .satisfies(e -> assertThat(e.getAvatar()).as("check avatar").isEqualTo(actual.getAvatar()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}