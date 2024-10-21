package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class JohnLennonAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJohnLennonAllPropertiesEquals(JohnLennon expected, JohnLennon actual) {
        assertJohnLennonAutoGeneratedPropertiesEquals(expected, actual);
        assertJohnLennonAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJohnLennonAllUpdatablePropertiesEquals(JohnLennon expected, JohnLennon actual) {
        assertJohnLennonUpdatableFieldsEquals(expected, actual);
        assertJohnLennonUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJohnLennonAutoGeneratedPropertiesEquals(JohnLennon expected, JohnLennon actual) {
        assertThat(expected)
            .as("Verify JohnLennon auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJohnLennonUpdatableFieldsEquals(JohnLennon expected, JohnLennon actual) {
        assertThat(expected)
            .as("Verify JohnLennon relevant properties")
            .satisfies(e -> assertThat(e.getProvider()).as("check provider").isEqualTo(actual.getProvider()))
            .satisfies(e -> assertThat(e.getProviderAppId()).as("check providerAppId").isEqualTo(actual.getProviderAppId()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getSlug()).as("check slug").isEqualTo(actual.getSlug()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJohnLennonUpdatableRelationshipsEquals(JohnLennon expected, JohnLennon actual) {
        assertThat(expected)
            .as("Verify JohnLennon relationships")
            .satisfies(e -> assertThat(e.getLogo()).as("check logo").isEqualTo(actual.getLogo()))
            .satisfies(e -> assertThat(e.getAppManager()).as("check appManager").isEqualTo(actual.getAppManager()))
            .satisfies(e -> assertThat(e.getOrganization()).as("check organization").isEqualTo(actual.getOrganization()))
            .satisfies(e -> assertThat(e.getJelloInitium()).as("check jelloInitium").isEqualTo(actual.getJelloInitium()))
            .satisfies(e -> assertThat(e.getInhouseInitium()).as("check inhouseInitium").isEqualTo(actual.getInhouseInitium()))
            .satisfies(e -> assertThat(e.getJelloInitiumVi()).as("check jelloInitiumVi").isEqualTo(actual.getJelloInitiumVi()))
            .satisfies(e -> assertThat(e.getInhouseInitiumVi()).as("check inhouseInitiumVi").isEqualTo(actual.getInhouseInitiumVi()));
    }
}