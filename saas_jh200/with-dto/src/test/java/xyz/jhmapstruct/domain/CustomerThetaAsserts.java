package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerThetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerThetaAllPropertiesEquals(CustomerTheta expected, CustomerTheta actual) {
        assertCustomerThetaAutoGeneratedPropertiesEquals(expected, actual);
        assertCustomerThetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerThetaAllUpdatablePropertiesEquals(CustomerTheta expected, CustomerTheta actual) {
        assertCustomerThetaUpdatableFieldsEquals(expected, actual);
        assertCustomerThetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerThetaAutoGeneratedPropertiesEquals(CustomerTheta expected, CustomerTheta actual) {
        assertThat(expected)
            .as("Verify CustomerTheta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerThetaUpdatableFieldsEquals(CustomerTheta expected, CustomerTheta actual) {
        assertThat(expected)
            .as("Verify CustomerTheta relevant properties")
            .satisfies(e -> assertThat(e.getFirstName()).as("check firstName").isEqualTo(actual.getFirstName()))
            .satisfies(e -> assertThat(e.getLastName()).as("check lastName").isEqualTo(actual.getLastName()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPhoneNumber()).as("check phoneNumber").isEqualTo(actual.getPhoneNumber()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerThetaUpdatableRelationshipsEquals(CustomerTheta expected, CustomerTheta actual) {
        assertThat(expected)
            .as("Verify CustomerTheta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}