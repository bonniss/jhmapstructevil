package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlLadyGagaViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaViAllPropertiesEquals(AlLadyGagaVi expected, AlLadyGagaVi actual) {
        assertAlLadyGagaViAutoGeneratedPropertiesEquals(expected, actual);
        assertAlLadyGagaViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaViAllUpdatablePropertiesEquals(AlLadyGagaVi expected, AlLadyGagaVi actual) {
        assertAlLadyGagaViUpdatableFieldsEquals(expected, actual);
        assertAlLadyGagaViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaViAutoGeneratedPropertiesEquals(AlLadyGagaVi expected, AlLadyGagaVi actual) {
        assertThat(expected)
            .as("Verify AlLadyGagaVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaViUpdatableFieldsEquals(AlLadyGagaVi expected, AlLadyGagaVi actual) {
        assertThat(expected)
            .as("Verify AlLadyGagaVi relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescriptionHeitiga()).as("check descriptionHeitiga").isEqualTo(actual.getDescriptionHeitiga()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlLadyGagaViUpdatableRelationshipsEquals(AlLadyGagaVi expected, AlLadyGagaVi actual) {
        assertThat(expected)
            .as("Verify AlLadyGagaVi relationships")
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getAvatar()).as("check avatar").isEqualTo(actual.getAvatar()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
