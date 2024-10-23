package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerBetaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerBetaAllPropertiesEquals(CustomerBeta expected, CustomerBeta actual) {
        assertCustomerBetaAutoGeneratedPropertiesEquals(expected, actual);
        assertCustomerBetaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerBetaAllUpdatablePropertiesEquals(CustomerBeta expected, CustomerBeta actual) {
        assertCustomerBetaUpdatableFieldsEquals(expected, actual);
        assertCustomerBetaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerBetaAutoGeneratedPropertiesEquals(CustomerBeta expected, CustomerBeta actual) {
        assertThat(expected)
            .as("Verify CustomerBeta auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerBetaUpdatableFieldsEquals(CustomerBeta expected, CustomerBeta actual) {
        assertThat(expected)
            .as("Verify CustomerBeta relevant properties")
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
    public static void assertCustomerBetaUpdatableRelationshipsEquals(CustomerBeta expected, CustomerBeta actual) {
        assertThat(expected)
            .as("Verify CustomerBeta relationships")
            .satisfies(e -> assertThat(e.getTenant()).as("check tenant").isEqualTo(actual.getTenant()));
    }
}
