package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlPowerShellAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPowerShellAllPropertiesEquals(AlPowerShell expected, AlPowerShell actual) {
        assertAlPowerShellAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPowerShellAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPowerShellAllUpdatablePropertiesEquals(AlPowerShell expected, AlPowerShell actual) {
        assertAlPowerShellUpdatableFieldsEquals(expected, actual);
        assertAlPowerShellUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPowerShellAutoGeneratedPropertiesEquals(AlPowerShell expected, AlPowerShell actual) {
        assertThat(expected)
            .as("Verify AlPowerShell auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPowerShellUpdatableFieldsEquals(AlPowerShell expected, AlPowerShell actual) {
        assertThat(expected)
            .as("Verify AlPowerShell relevant properties")
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPowerShellUpdatableRelationshipsEquals(AlPowerShell expected, AlPowerShell actual) {
        assertThat(expected)
            .as("Verify AlPowerShell relationships")
            .satisfies(e -> assertThat(e.getPropertyProfile()).as("check propertyProfile").isEqualTo(actual.getPropertyProfile()))
            .satisfies(e -> assertThat(e.getAttributeTerm()).as("check attributeTerm").isEqualTo(actual.getAttributeTerm()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
