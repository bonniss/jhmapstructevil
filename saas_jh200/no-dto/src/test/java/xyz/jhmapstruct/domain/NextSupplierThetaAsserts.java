package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NextSupplierThetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierThetaAllPropertiesEquals(NextSupplierTheta expected, NextSupplierTheta actual) {
        assertNextSupplierThetaAutoGeneratedPropertiesEquals(expected, actual);
        assertNextSupplierThetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierThetaAllUpdatablePropertiesEquals(NextSupplierTheta expected, NextSupplierTheta actual) {
        assertNextSupplierThetaUpdatableFieldsEquals(expected, actual);
        assertNextSupplierThetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierThetaAutoGeneratedPropertiesEquals(NextSupplierTheta expected, NextSupplierTheta actual) {
        assertThat(expected)
            .as("Verify NextSupplierTheta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierThetaUpdatableFieldsEquals(NextSupplierTheta expected, NextSupplierTheta actual) {
        assertThat(expected)
            .as("Verify NextSupplierTheta relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getContactPerson()).as("check contactPerson").isEqualTo(actual.getContactPerson()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPhoneNumber()).as("check phoneNumber").isEqualTo(actual.getPhoneNumber()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNextSupplierThetaUpdatableRelationshipsEquals(NextSupplierTheta expected, NextSupplierTheta actual) {
        assertThat(expected)
            .as("Verify NextSupplierTheta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()))
            .satisfies(e -> assertThat(e.getProducts()).as("check products").isEqualTo(actual.getProducts()));
    }
}
