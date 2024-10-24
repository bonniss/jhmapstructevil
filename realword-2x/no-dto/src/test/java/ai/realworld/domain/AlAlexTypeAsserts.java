package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlAlexTypeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAlexTypeAllPropertiesEquals(AlAlexType expected, AlAlexType actual) {
        assertAlAlexTypeAutoGeneratedPropertiesEquals(expected, actual);
        assertAlAlexTypeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAlexTypeAllUpdatablePropertiesEquals(AlAlexType expected, AlAlexType actual) {
        assertAlAlexTypeUpdatableFieldsEquals(expected, actual);
        assertAlAlexTypeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAlexTypeAutoGeneratedPropertiesEquals(AlAlexType expected, AlAlexType actual) {
        assertThat(expected)
            .as("Verify AlAlexType auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAlexTypeUpdatableFieldsEquals(AlAlexType expected, AlAlexType actual) {
        assertThat(expected)
            .as("Verify AlAlexType relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getCanDoRetail()).as("check canDoRetail").isEqualTo(actual.getCanDoRetail()))
            .satisfies(e -> assertThat(e.getIsOrgDivision()).as("check isOrgDivision").isEqualTo(actual.getIsOrgDivision()))
            .satisfies(e -> assertThat(e.getConfigJason()).as("check configJason").isEqualTo(actual.getConfigJason()))
            .satisfies(e -> assertThat(e.getTreeDepth()).as("check treeDepth").isEqualTo(actual.getTreeDepth()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAlexTypeUpdatableRelationshipsEquals(AlAlexType expected, AlAlexType actual) {
        assertThat(expected)
            .as("Verify AlAlexType relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
