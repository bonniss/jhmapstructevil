package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlPedroTaxAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPedroTaxAllPropertiesEquals(AlPedroTax expected, AlPedroTax actual) {
        assertAlPedroTaxAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPedroTaxAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPedroTaxAllUpdatablePropertiesEquals(AlPedroTax expected, AlPedroTax actual) {
        assertAlPedroTaxUpdatableFieldsEquals(expected, actual);
        assertAlPedroTaxUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPedroTaxAutoGeneratedPropertiesEquals(AlPedroTax expected, AlPedroTax actual) {
        assertThat(expected)
            .as("Verify AlPedroTax auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPedroTaxUpdatableFieldsEquals(AlPedroTax expected, AlPedroTax actual) {
        assertThat(expected)
            .as("Verify AlPedroTax relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getWeight()).as("check weight").isEqualTo(actual.getWeight()))
            .satisfies(e -> assertThat(e.getPropertyType()).as("check propertyType").isEqualTo(actual.getPropertyType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPedroTaxUpdatableRelationshipsEquals(AlPedroTax expected, AlPedroTax actual) {
        assertThat(expected)
            .as("Verify AlPedroTax relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
