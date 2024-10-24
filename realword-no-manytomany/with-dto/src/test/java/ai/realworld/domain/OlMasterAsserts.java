package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OlMasterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOlMasterAllPropertiesEquals(OlMaster expected, OlMaster actual) {
        assertOlMasterAutoGeneratedPropertiesEquals(expected, actual);
        assertOlMasterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOlMasterAllUpdatablePropertiesEquals(OlMaster expected, OlMaster actual) {
        assertOlMasterUpdatableFieldsEquals(expected, actual);
        assertOlMasterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOlMasterAutoGeneratedPropertiesEquals(OlMaster expected, OlMaster actual) {
        assertThat(expected)
            .as("Verify OlMaster auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOlMasterUpdatableFieldsEquals(OlMaster expected, OlMaster actual) {
        assertThat(expected)
            .as("Verify OlMaster relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getSlug()).as("check slug").isEqualTo(actual.getSlug()))
            .satisfies(e -> assertThat(e.getDescriptionHeitiga()).as("check descriptionHeitiga").isEqualTo(actual.getDescriptionHeitiga()))
            .satisfies(e -> assertThat(e.getBusinessType()).as("check businessType").isEqualTo(actual.getBusinessType()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getHotline()).as("check hotline").isEqualTo(actual.getHotline()))
            .satisfies(e -> assertThat(e.getTaxCode()).as("check taxCode").isEqualTo(actual.getTaxCode()))
            .satisfies(e -> assertThat(e.getContactsJason()).as("check contactsJason").isEqualTo(actual.getContactsJason()))
            .satisfies(e -> assertThat(e.getExtensionJason()).as("check extensionJason").isEqualTo(actual.getExtensionJason()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOlMasterUpdatableRelationshipsEquals(OlMaster expected, OlMaster actual) {
        assertThat(expected)
            .as("Verify OlMaster relationships")
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()));
    }
}
