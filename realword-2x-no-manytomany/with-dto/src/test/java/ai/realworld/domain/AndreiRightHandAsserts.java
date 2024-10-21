package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AndreiRightHandAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAndreiRightHandAllPropertiesEquals(AndreiRightHand expected, AndreiRightHand actual) {
        assertAndreiRightHandAutoGeneratedPropertiesEquals(expected, actual);
        assertAndreiRightHandAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAndreiRightHandAllUpdatablePropertiesEquals(AndreiRightHand expected, AndreiRightHand actual) {
        assertAndreiRightHandUpdatableFieldsEquals(expected, actual);
        assertAndreiRightHandUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAndreiRightHandAutoGeneratedPropertiesEquals(AndreiRightHand expected, AndreiRightHand actual) {
        assertThat(expected)
            .as("Verify AndreiRightHand auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAndreiRightHandUpdatableFieldsEquals(AndreiRightHand expected, AndreiRightHand actual) {
        assertThat(expected)
            .as("Verify AndreiRightHand relevant properties")
            .satisfies(e -> assertThat(e.getDetails()).as("check details").isEqualTo(actual.getDetails()))
            .satisfies(e -> assertThat(e.getLat()).as("check lat").isEqualTo(actual.getLat()))
            .satisfies(e -> assertThat(e.getLon()).as("check lon").isEqualTo(actual.getLon()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAndreiRightHandUpdatableRelationshipsEquals(AndreiRightHand expected, AndreiRightHand actual) {
        assertThat(expected)
            .as("Verify AndreiRightHand relationships")
            .satisfies(e -> assertThat(e.getCountry()).as("check country").isEqualTo(actual.getCountry()))
            .satisfies(e -> assertThat(e.getProvince()).as("check province").isEqualTo(actual.getProvince()))
            .satisfies(e -> assertThat(e.getDistrict()).as("check district").isEqualTo(actual.getDistrict()))
            .satisfies(e -> assertThat(e.getWard()).as("check ward").isEqualTo(actual.getWard()));
    }
}
