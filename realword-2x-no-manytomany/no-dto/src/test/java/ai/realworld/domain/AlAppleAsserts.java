package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlAppleAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAppleAllPropertiesEquals(AlApple expected, AlApple actual) {
        assertAlAppleAutoGeneratedPropertiesEquals(expected, actual);
        assertAlAppleAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAppleAllUpdatablePropertiesEquals(AlApple expected, AlApple actual) {
        assertAlAppleUpdatableFieldsEquals(expected, actual);
        assertAlAppleUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAppleAutoGeneratedPropertiesEquals(AlApple expected, AlApple actual) {
        assertThat(expected)
            .as("Verify AlApple auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlAppleUpdatableFieldsEquals(AlApple expected, AlApple actual) {
        assertThat(expected)
            .as("Verify AlApple relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
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
    public static void assertAlAppleUpdatableRelationshipsEquals(AlApple expected, AlApple actual) {
        assertThat(expected)
            .as("Verify AlApple relationships")
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getAgencyType()).as("check agencyType").isEqualTo(actual.getAgencyType()))
            .satisfies(e -> assertThat(e.getLogo()).as("check logo").isEqualTo(actual.getLogo()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
