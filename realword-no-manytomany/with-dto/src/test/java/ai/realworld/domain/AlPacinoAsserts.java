package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlPacinoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoAllPropertiesEquals(AlPacino expected, AlPacino actual) {
        assertAlPacinoAutoGeneratedPropertiesEquals(expected, actual);
        assertAlPacinoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoAllUpdatablePropertiesEquals(AlPacino expected, AlPacino actual) {
        assertAlPacinoUpdatableFieldsEquals(expected, actual);
        assertAlPacinoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoAutoGeneratedPropertiesEquals(AlPacino expected, AlPacino actual) {
        assertThat(expected)
            .as("Verify AlPacino auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoUpdatableFieldsEquals(AlPacino expected, AlPacino actual) {
        assertThat(expected)
            .as("Verify AlPacino relevant properties")
            .satisfies(e -> assertThat(e.getPlatformCode()).as("check platformCode").isEqualTo(actual.getPlatformCode()))
            .satisfies(e -> assertThat(e.getPlatformUsername()).as("check platformUsername").isEqualTo(actual.getPlatformUsername()))
            .satisfies(e -> assertThat(e.getPlatformAvatarUrl()).as("check platformAvatarUrl").isEqualTo(actual.getPlatformAvatarUrl()))
            .satisfies(e -> assertThat(e.getIsSensitive()).as("check isSensitive").isEqualTo(actual.getIsSensitive()))
            .satisfies(e -> assertThat(e.getFamilyName()).as("check familyName").isEqualTo(actual.getFamilyName()))
            .satisfies(e -> assertThat(e.getGivenName()).as("check givenName").isEqualTo(actual.getGivenName()))
            .satisfies(e -> assertThat(e.getDob()).as("check dob").isEqualTo(actual.getDob()))
            .satisfies(e -> assertThat(e.getGender()).as("check gender").isEqualTo(actual.getGender()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getAcquiredFrom()).as("check acquiredFrom").isEqualTo(actual.getAcquiredFrom()))
            .satisfies(e -> assertThat(e.getCurrentPoints()).as("check currentPoints").isEqualTo(actual.getCurrentPoints()))
            .satisfies(e -> assertThat(e.getTotalPoints()).as("check totalPoints").isEqualTo(actual.getTotalPoints()))
            .satisfies(e -> assertThat(e.getIsFollowing()).as("check isFollowing").isEqualTo(actual.getIsFollowing()))
            .satisfies(e -> assertThat(e.getIsEnabled()).as("check isEnabled").isEqualTo(actual.getIsEnabled()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlPacinoUpdatableRelationshipsEquals(AlPacino expected, AlPacino actual) {
        assertThat(expected)
            .as("Verify AlPacino relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()))
            .satisfies(e -> assertThat(e.getMembershipTier()).as("check membershipTier").isEqualTo(actual.getMembershipTier()))
            .satisfies(e -> assertThat(e.getAlVueVueUsage()).as("check alVueVueUsage").isEqualTo(actual.getAlVueVueUsage()));
    }
}
