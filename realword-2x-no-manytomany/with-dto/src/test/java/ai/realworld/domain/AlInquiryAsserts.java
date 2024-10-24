package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlInquiryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlInquiryAllPropertiesEquals(AlInquiry expected, AlInquiry actual) {
        assertAlInquiryAutoGeneratedPropertiesEquals(expected, actual);
        assertAlInquiryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlInquiryAllUpdatablePropertiesEquals(AlInquiry expected, AlInquiry actual) {
        assertAlInquiryUpdatableFieldsEquals(expected, actual);
        assertAlInquiryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlInquiryAutoGeneratedPropertiesEquals(AlInquiry expected, AlInquiry actual) {
        assertThat(expected)
            .as("Verify AlInquiry auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlInquiryUpdatableFieldsEquals(AlInquiry expected, AlInquiry actual) {
        assertThat(expected)
            .as("Verify AlInquiry relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getBody()).as("check body").isEqualTo(actual.getBody()))
            .satisfies(e -> assertThat(e.getSender()).as("check sender").isEqualTo(actual.getSender()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getContentJason()).as("check contentJason").isEqualTo(actual.getContentJason()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlInquiryUpdatableRelationshipsEquals(AlInquiry expected, AlInquiry actual) {
        assertThat(expected)
            .as("Verify AlInquiry relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getAgency()).as("check agency").isEqualTo(actual.getAgency()))
            .satisfies(e -> assertThat(e.getPersonInCharge()).as("check personInCharge").isEqualTo(actual.getPersonInCharge()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
