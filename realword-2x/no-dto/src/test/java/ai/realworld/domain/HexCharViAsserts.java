package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HexCharViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHexCharViAllPropertiesEquals(HexCharVi expected, HexCharVi actual) {
        assertHexCharViAutoGeneratedPropertiesEquals(expected, actual);
        assertHexCharViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHexCharViAllUpdatablePropertiesEquals(HexCharVi expected, HexCharVi actual) {
        assertHexCharViUpdatableFieldsEquals(expected, actual);
        assertHexCharViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHexCharViAutoGeneratedPropertiesEquals(HexCharVi expected, HexCharVi actual) {
        assertThat(expected)
            .as("Verify HexCharVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHexCharViUpdatableFieldsEquals(HexCharVi expected, HexCharVi actual) {
        assertThat(expected)
            .as("Verify HexCharVi relevant properties")
            .satisfies(e -> assertThat(e.getDob()).as("check dob").isEqualTo(actual.getDob()))
            .satisfies(e -> assertThat(e.getGender()).as("check gender").isEqualTo(actual.getGender()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getBioHeitiga()).as("check bioHeitiga").isEqualTo(actual.getBioHeitiga()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHexCharViUpdatableRelationshipsEquals(HexCharVi expected, HexCharVi actual) {
        assertThat(expected)
            .as("Verify HexCharVi relationships")
            .satisfies(e -> assertThat(e.getRole()).as("check role").isEqualTo(actual.getRole()));
    }
}
