package ai.realworld.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlGoogleMeetViAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoogleMeetViAllPropertiesEquals(AlGoogleMeetVi expected, AlGoogleMeetVi actual) {
        assertAlGoogleMeetViAutoGeneratedPropertiesEquals(expected, actual);
        assertAlGoogleMeetViAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoogleMeetViAllUpdatablePropertiesEquals(AlGoogleMeetVi expected, AlGoogleMeetVi actual) {
        assertAlGoogleMeetViUpdatableFieldsEquals(expected, actual);
        assertAlGoogleMeetViUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoogleMeetViAutoGeneratedPropertiesEquals(AlGoogleMeetVi expected, AlGoogleMeetVi actual) {
        assertThat(expected)
            .as("Verify AlGoogleMeetVi auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoogleMeetViUpdatableFieldsEquals(AlGoogleMeetVi expected, AlGoogleMeetVi actual) {
        assertThat(expected)
            .as("Verify AlGoogleMeetVi relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e ->
                assertThat(e.getNumberOfParticipants()).as("check numberOfParticipants").isEqualTo(actual.getNumberOfParticipants())
            )
            .satisfies(e -> assertThat(e.getPlannedDate()).as("check plannedDate").isEqualTo(actual.getPlannedDate()))
            .satisfies(e ->
                assertThat(e.getPlannedDurationInMinutes())
                    .as("check plannedDurationInMinutes")
                    .isEqualTo(actual.getPlannedDurationInMinutes())
            )
            .satisfies(e -> assertThat(e.getActualDate()).as("check actualDate").isEqualTo(actual.getActualDate()))
            .satisfies(e ->
                assertThat(e.getActualDurationInMinutes())
                    .as("check actualDurationInMinutes")
                    .isEqualTo(actual.getActualDurationInMinutes())
            )
            .satisfies(e -> assertThat(e.getContentJason()).as("check contentJason").isEqualTo(actual.getContentJason()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlGoogleMeetViUpdatableRelationshipsEquals(AlGoogleMeetVi expected, AlGoogleMeetVi actual) {
        assertThat(expected)
            .as("Verify AlGoogleMeetVi relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getAgency()).as("check agency").isEqualTo(actual.getAgency()))
            .satisfies(e -> assertThat(e.getPersonInCharge()).as("check personInCharge").isEqualTo(actual.getPersonInCharge()))
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
